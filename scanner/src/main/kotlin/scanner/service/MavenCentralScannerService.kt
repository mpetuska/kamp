package scanner.service

import kamp.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.processor.*
import scanner.util.*
import kotlin.time.*

class MavenCentralScannerService(
  override val client: MavenRepositoryClient<MavenArtifactImpl>,
  override val pomProcessor: PomProcessor,
  override val gradleModuleProcessor: GradleModuleProcessor,
) : MavenScannerService<MavenArtifactImpl>() {
  override fun CoroutineScope.produceArtifacts(rootArtefactsFilter: Set<String>?): ReceiveChannel<MavenArtifactImpl> = produce {
    val pageChannel = Channel<List<MavenRepositoryClient.RepoItem>>(Channel.BUFFERED)
    supervisedLaunch {
      client.listRepositoryPath("")?.filter { repoItem ->
        rootArtefactsFilter
          ?.map { it.removePrefix("/") }
          ?.let { filter -> filter.any { repoItem.path.removePrefix("/").startsWith(it) } }
          ?: true
      }?.let { pageChannel.send(it) }
    }
    
    // Tracker
    supervisedLaunch {
      var ticks = 0
      do {
        delay(30.seconds)
        if (pageChannel.isEmpty) {
          logger.info("Page channel empty, ${5 - ticks} ticks remaining until close")
          ticks++
        } else {
          ticks = 0
        }
      } while (ticks < 5)
      logger.info("Closing page channel")
      pageChannel.close()
      logger.info("Closed page channel")
    }
    
    // Workers
    repeat(Runtime.getRuntime().availableProcessors() * 2) {
      supervisedLaunch {
        for (page in pageChannel) {
          val artifactDetails = page.find { it.value == "maven-metadata.xml" }?.let {
            client.getArtifactDetails(it.path)
          }
          if (artifactDetails != null) {
            logger.debug("Found MC artefact ${artifactDetails.group}:${artifactDetails.name}")
            send(artifactDetails)
          } else {
            page
              .filter(MavenRepositoryClient.RepoItem::isDirectory)
              .map {
                supervisedLaunch {
                  client.listRepositoryPath(it.path)?.let { item ->
                    logger.debug("Scanned MC page ${it.path} and found ${item.size} children")
                    pageChannel.send(item)
                  }
                }
              }
          }
        }
      }
    }
  }
}
