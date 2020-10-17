package scanner.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.domain.mc.*
import scanner.util.*

object MCScannerService : ScannerService<MCArtifact>() {
  override val client = MavenCentralClient
  
  override fun CoroutineScope.produceArtifactChannel(): ReceiveChannel<MCArtifact> =
    produce {
      val pages = produce {
        client.listRepositoryPath("").filter { it.isDirectory }.forEach {
          errorSafe {
            send(client.listRepositoryPath(it.path))
          }
        }
      }
      parallel {
        for (page in pages) {
          errorSafe {
            scanRepoPage(page)
          }
        }
      }
    }
  
  private suspend fun SendChannel<MCArtifact>.scanRepoPage(page: List<RepoItem>) {
    val mavenMetadata = page.find { it.value == "maven-metadata.xml" }
    if (mavenMetadata != null) {
      client.getArtifactDetails(mavenMetadata.path)?.let {
        send(MCArtifact(it.group, it.name, it.latestVersion))
      }
    } else {
      coroutineScope {
        page.filter { it.isDirectory }.forEach {
          errorSafe {
            logger.debug { "Scanning MC page ${it.path}" }
            val subpage = client.listRepositoryPath(it.path)
            scanRepoPage(subpage)
          }
        }
      }
    }
  }
}
