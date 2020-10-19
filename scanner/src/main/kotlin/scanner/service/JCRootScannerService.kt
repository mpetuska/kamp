package scanner.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.domain.jc.*
import scanner.util.*

object JCRootScannerService : ScannerService<JCArtifact>() {
  private const val url = "https://repo.jfrog.org/artifactory/libs-release-bintray/"
  override val client = JCenterClient
  
  private suspend fun ProducerScope<String>.generatePrefixes(
    length: Int,
    prefix: String = "",
    onEach: suspend (String) -> Unit,
  ): Unit = if (length > 1) {
    ('a'..'z').forEach { generatePrefixes(length - 1, "$prefix$it", onEach) }
  } else {
    ('a'..'z').forEach { onEach("$prefix$it") }
  }
  
  override fun CoroutineScope.produceArtifactChannel(): ReceiveChannel<JCArtifact> {
    return produce(capacity = 16) {
      val pages = produce(capacity = 16) {
        val roots = produce(capacity = 16) {
          generatePrefixes(4) {
            send(it)
          }
          MavenCentralClient.listRepositoryPath("").forEach {
            send(it.value.removeSuffix("/"))
          }
        }
        roots.consumeSafe { root ->
          client.listRepositoryPath("/$root").filter { it.isDirectory }.forEach {
            errorSafe {
              send(client.listRepositoryPath(it.path))
            }
          }
        }
      }
      parallel {
        pages.consumeSafe { page ->
          errorSafe {
            scanRepoPage(page)
          }
        }
      }
    }
  }
  
  private suspend fun SendChannel<JCArtifact>.scanRepoPage(page: List<RepoItem>) {
    val mavenMetadata = page.find { it.value == "maven-metadata.xml" }
    if (mavenMetadata != null) {
      client.getArtifactDetails(mavenMetadata.path)?.let {
        send(JCArtifact(JCPackage("bintray", "jcenter", it.name), it.group, it.name, it.latestVersion))
      }
    } else {
      coroutineScope {
        page.filter { it.isDirectory }.forEach {
          errorSafe {
            logger.info { "Scanning JC page ${it.path}" }
            val subpage = client.listRepositoryPath(it.path)
            scanRepoPage(subpage)
          }
        }
      }
    }
  }
}
