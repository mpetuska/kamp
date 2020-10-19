package scanner.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.domain.mc.*
import scanner.util.*

object MCScannerService : ScannerService<MCArtifact>() {
  override val client = MavenCentralClient
  
  override fun CoroutineScope.produceArtifactChannel(): ReceiveChannel<MCArtifact> =
    produce(capacity = 16) {
      val pages = produce(capacity = 16) {
        val repos = client.listRepositoryPath("")
        val filtered = repos.filter { it.isDirectory }
        filtered.forEach {
          errorSafe {
            send(client.listRepositoryPath(it.path))
          }
        }
      }
      parallel {
        pages.consumeSafe { page ->
          scanRepoPage(page)
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
        val subpages = produce {
          page.filter { it.isDirectory }.forEach {
            errorSafe {
              logger.info { "Scanning MC page ${it.path}" }
              val subpage = client.listRepositoryPath(it.path)
              send(subpage)
            }
          }
        }
        parallel {
          subpages.consumeSafe { subpage ->
            scanRepoPage(subpage)
          }
        }
      }
    }
  }
}
