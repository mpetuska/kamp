package scanner.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.domain.mc.*
import scanner.processor.*
import scanner.util.*
import kotlin.time.*

class MavenCentralScannerService(
  override val client: MavenRepositoryClient<MCArtifact>,
  override val pomProcessor: PomProcessor,
  override val gradleModuleProcessor: GradleModuleProcessor,
) : MavenScannerService<MCArtifact>() {
  override fun CoroutineScope.produceArtifacts(): ReceiveChannel<MCArtifact> = produce {
    val pageChannel = Channel<List<MavenRepositoryClient.RepoItem>>(Channel.BUFFERED)
    supervisedLaunch {
      client.listRepositoryPath("")?.let { pageChannel.send(it) }
    }
    
    // Tracker
    supervisedLaunch {
      var ticks = 0
      do {
        delay(30.seconds)
        if (pageChannel.isEmpty) {
          ticks++
          logger.info("Page channel empty, ${5 - ticks} ticks remaining until close")
        } else {
          ticks = 0
        }
      } while (ticks < 5)
      pageChannel.close()
    }
    
    // Workers
    List(Runtime.getRuntime().availableProcessors() * 2) {
      supervisedLaunch {
        for (page in pageChannel) {
          val mavenMetadata = page.find { it.value == "maven-metadata.xml" }
          if (mavenMetadata != null) {
            client.getArtifactDetails(mavenMetadata.path)?.let {
              send(MCArtifact(it.group, it.name, it.latestVersion))
            }
          } else {
            page
              .filter(MavenRepositoryClient.RepoItem::isDirectory)
              .map {
                supervisedLaunch {
                  client.listRepositoryPath(it.path)?.let { item ->
                    logger.info("Scanning MC page ${it.path}")
                    pageChannel.send(item)
                  }
                }
              }
          }
        }
      }
    }.joinAll()
  }
}
