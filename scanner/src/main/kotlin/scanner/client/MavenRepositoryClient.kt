package scanner.client

import io.ktor.client.*
import io.ktor.client.request.*
import kamp.domain.*
import kotlinx.coroutines.*
import org.jsoup.nodes.*
import org.kodein.di.*
import scanner.domain.*
import scanner.util.*

abstract class MavenRepositoryClient<A : MavenArtifact>(
  private val defaultRepositoryRootUrl: String,
) : DIAware {
  protected abstract fun parsePage(page: Document): List<String>?
  
  private val A.mavenModuleRootUrl: String
    get() = "$defaultRepositoryRootUrl/${group.replace(".", "/")}/$name"
  private val A.mavenModuleVersionUrl: String
    get() = "$mavenModuleRootUrl/$latestVersion"
  
  suspend fun getArtifactDetails(pathToMavenMetadata: String): MavenArtifact? =
    coroutineScope {
      val client by di.instance<HttpClient>()
      val artifact = asyncOrNull {
        val pom = client.get<String>("$defaultRepositoryRootUrl$pathToMavenMetadata").asDocument()
        val doc = pom.getElementsByTag("metadata").first()
        listOfNotNull(
          doc.selectFirst("groupId")?.text(),
          doc.selectFirst("artifactId")?.text(),
          doc.selectFirst("versioning>latest")?.text() ?: doc.selectFirst("version")?.text()
        ).takeIf { it.size == 3 }?.let {
          object : MavenArtifact {
            override val group: String = it[0]
            override val name: String = it[1]
            override val latestVersion: String = it[2]
          }
        }
      }
      
      artifact.await()
    }
  
  
  suspend fun getLatestVersion(artifact: A): String? = coroutineScope {
    asyncOrNull {
      val client by di.instance<HttpClient>()
      client.get<String>("${artifact.mavenModuleRootUrl}/maven-metadata.xml").asDocument().selectFirst("metadata>versioning>latest")?.text()
    }.await()
  }
  
  
  suspend fun getGradleModule(artifact: A): GradleModule? = coroutineScope {
    asyncOrNull {
      val client by di.instance<HttpClient>()
      client.get<GradleModule>("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.latestVersion}.module")
    }.await()
  }
  
  suspend fun getMavenPom(artifact: A): Document? = coroutineScope {
    asyncOrNull {
      val client by di.instance<HttpClient>()
      client.get<String>("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.latestVersion}.pom").asDocument()
    }.await()
  }
  
  suspend fun listRepositoryPath(path: String): List<RepoItem>? = coroutineScope {
    asyncOrNull {
      val client by di.instance<HttpClient>()
      client.get<String>("$defaultRepositoryRootUrl$path").let { str ->
        parsePage(str.asDocument())?.mapNotNull { RepoItem(it, path).takeUnless { v -> v.value.startsWith("..") } }
      }
    }.await()
  }
  
  class RepoItem(val value: String, path: String) {
    val parentPath = "/${path.removeSuffix("/").removePrefix("/")}"
    val path = "${if (parentPath == "/") "" else parentPath}/$value".removeSuffix("/")
    val isFile = !value.endsWith("/")
    val isDirectory = value.endsWith("/")
    
    override fun toString() = value
  }
}
