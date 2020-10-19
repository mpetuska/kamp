package scanner.client

import kamp.domain.*
import org.jsoup.nodes.*
import scanner.domain.*
import scanner.util.*

abstract class MavenRepositoryClient<A : MavenArtifact> {
  private val A.mavenModuleRootUrl: String
    get() = "$defaultRepositoryRootUrl/${group.replace(".", "/")}/${name.replace(".", "/")}"
  private val A.mavenModuleVersionUrl: String
    get() = "$mavenModuleRootUrl/$latestVersion"
  
  suspend fun getArtifactDetails(pathToMavenMetadata: String): MavenArtifact? =
    fetch<String>("$defaultRepositoryRootUrl$pathToMavenMetadata")?.let { res ->
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
  
  suspend fun getLatestVersion(artifact: A): String? =
    fetch<String>("${artifact.mavenModuleRootUrl}/maven-metadata.xml")?.let { res ->
      res.asDocument().selectFirst("metadata>versioning>latest")?.text()
    }
  
  suspend fun getGradleModule(artifact: A): GradleModule? =
    fetch<GradleModule>("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.latestVersion}.module")
  
  suspend fun getMavenPom(artifact: A): Document? =
    fetch<String>("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.latestVersion}.pom")?.asDocument()
  
  suspend fun listRepositoryPath(path: String): List<RepoItem> =
    fetch<String>("$defaultRepositoryRootUrl$path")?.let { str ->
      parsePage(str.asDocument()).mapNotNull { RepoItem(it, path).takeUnless { v -> v.value.startsWith("..") } }
    } ?: listOf()
  
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
