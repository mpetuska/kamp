package scanner.client

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.utils.io.core.*
import kamp.domain.*
import kotlinx.coroutines.*
import org.jsoup.nodes.*
import scanner.domain.*
import scanner.util.*

abstract class MavenRepositoryClient<A : MavenArtifact>(
  private val defaultRepositoryRootUrl: String,
) : Closeable {
  protected abstract fun parsePage(page: Document): List<String>?
  protected abstract val client: HttpClient
  
  private val A.mavenModuleRootUrl: String
    get() = "$defaultRepositoryRootUrl/${group.replace(".", "/")}/$name"
  private val A.mavenModuleVersionUrl: String
    get() = "$mavenModuleRootUrl/$latestVersion"
  
  suspend fun getArtifactDetails(pathToMavenMetadata: String): MavenArtifact? =
    coroutineScope {
      val artifact = supervisedAsync {
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
    supervisedAsync {
      client.get<String>("${artifact.mavenModuleRootUrl}/maven-metadata.xml").asDocument().selectFirst("metadata>versioning>latest")?.text()
    }.await()
  }
  
  
  suspend fun getGradleModule(artifact: A): GradleModule? = coroutineScope {
    supervisedAsync {
      client.get<GradleModule>("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.latestVersion}.module")
    }.await()
  }
  
  suspend fun getMavenPom(artifact: A): Document? = coroutineScope {
    supervisedAsync {
      client.get<String>("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.latestVersion}.pom").asDocument()
    }.await()
  }
  
  suspend fun listRepositoryPath(path: String): List<RepoItem>? = coroutineScope {
    supervisedAsync {
      client.get<String>("$defaultRepositoryRootUrl$path").let { str ->
        parsePage(str.asDocument())?.mapNotNull { RepoItem(it, path).takeUnless { v -> v.value.startsWith("..") } }
      }
    }.await()
  }
  
  override fun close() = client.close()
  
  class RepoItem(val value: String, path: String) {
    val parentPath = "/${path.removeSuffix("/").removePrefix("/")}"
    val path = "${if (parentPath == "/") "" else parentPath}/$value".removeSuffix("/")
    val isDirectory = value.endsWith("/")
    val isFile = !isDirectory
    
    override fun toString() = value
  }
}
