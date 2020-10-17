package scanner.client

import io.ktor.client.request.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.jsoup.nodes.*
import scanner.domain.*
import scanner.util.*

abstract class MavenRepositoryClient<A : MavenArtifact> {
  private val A.mavenModuleRootUrl: String
    get() = "$defaultRepositoryRootUrl/${group.replace(".", "/")}/${name.replace(".", "/")}"
  private val A.mavenModuleVersionUrl: String
    get() = "$mavenModuleRootUrl/$latestVersion"
  
  suspend fun getArtifactDetails(pathToMavenMetadata: String): MavenArtifact? = httpClientWithNotFound {
    val res = get<String>("$defaultRepositoryRootUrl$pathToMavenMetadata")
    val doc = res.asDocument().getElementsByTag("metadata").first()
    listOfNotNull(
      doc.selectFirst("groupId")?.text(),
      doc.selectFirst("artifactId")?.text(),
      doc.selectFirst("versioning>latest")?.text() ?: doc.selectFirst("version")?.text()
    ).takeIf { it.size == 3 }?.let {
      object : MavenArtifact() {
        override val group: String = it[0]
        override val name: String = it[1]
        override val latestVersion: String = it[2]
      }
    }
  }
  
  suspend fun getLatestVersion(artifact: A): String? = httpClientWithNotFound {
    val res = get<String>("${artifact.mavenModuleRootUrl}/maven-metadata.xml")
    res.asDocument().selectFirst("metadata>versioning>latest")?.text()
  }
  
  suspend fun getGradleModule(artifact: A): GradleModule? = httpClientWithNotFound {
    val moduleStr = get<String>("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.latestVersion}.module")
    Json {
      ignoreUnknownKeys = true
    }.decodeFromString<GradleModule>(moduleStr)
  }
  
  suspend fun getMavenPom(artifact: A): Document? = httpClientWithNotFound {
    val moduleStr = get<String>("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.latestVersion}.pom")
    moduleStr.asDocument()
  }
  
  suspend fun listRepositoryPath(path: String): List<RepoItem> = httpClientWithNotFound(listOf()) {
    val str = get<String>("$defaultRepositoryRootUrl$path")
    parsePage(str.asDocument()).mapNotNull { RepoItem(it, path).takeUnless { v -> v.value.startsWith("..") } }
  }
  
  protected abstract val A.repositoryRootUrl: String
  protected abstract val defaultRepositoryRootUrl: String
  
  protected abstract fun parsePage(page: Document): List<String>
}

class RepoItem(val value: String, path: String) {
  val parentPath = "/${path.removeSuffix("/").removePrefix("/")}"
  val path = "${if (parentPath == "/") "" else parentPath}/$value".removeSuffix("/")
  val isFile = !value.endsWith("/")
  val isDirectory = value.endsWith("/")
  
  override fun toString() = value
}
