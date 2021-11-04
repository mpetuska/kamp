package dev.petuska.kamp.cli.service

import dev.petuska.kamp.cli.client.MavenRepositoryClient
import dev.petuska.kamp.cli.domain.CLIOptions
import dev.petuska.kamp.cli.processor.GradleModuleProcessor
import dev.petuska.kamp.cli.processor.PomProcessor
import dev.petuska.kamp.cli.util.supervisedLaunch
import dev.petuska.kamp.core.domain.SimpleMavenArtefact
import kotlin.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

class MavenScannerServiceImpl(
    override val client: MavenRepositoryClient<SimpleMavenArtefact>,
    override val pomProcessor: PomProcessor,
    override val gradleModuleProcessor: GradleModuleProcessor,
) : MavenScannerService<SimpleMavenArtefact>() {
  override fun CoroutineScope.produceArtifacts(
      cliOptions: CLIOptions?,
  ): ReceiveChannel<SimpleMavenArtefact> = produce {
    val pageChannel = Channel<List<MavenRepositoryClient.RepoItem>>(Channel.BUFFERED)
    supervisedLaunch {
      client
          .listRepositoryPath("")
          ?.filter { repoItem ->
            val path = repoItem.path.removePrefix("/")
            val included =
                cliOptions?.include?.let { filter -> filter.any { path.startsWith(it) } } ?: true
            val excluded =
                cliOptions?.exclude?.let { filter -> filter.any { path.startsWith(it) } } ?: false
            included && !excluded
          }
          ?.let { pageChannel.send(it) }
    }

    // Tracker
    supervisedLaunch {
      var ticks = 0
      do {
        delay(cliOptions?.delayMS?.let { Duration.milliseconds(it) } ?: Duration.seconds(10))
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
    repeat(cliOptions?.workers ?: (Runtime.getRuntime().availableProcessors() * 2)) {
      supervisedLaunch {
        for (page in pageChannel) {
          cliOptions?.delayMS?.let { delay(Duration.milliseconds(it)) }
          val artifactDetails =
              page.find { it.value == "maven-metadata.xml" }?.let {
                client.getArtifactDetails(it.path)
              }
          if (artifactDetails != null) {
            logger.debug("Found MC artefact ${artifactDetails.group}:${artifactDetails.name}")
            send(artifactDetails)
          } else {
            page.filter(MavenRepositoryClient.RepoItem::isDirectory).map {
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
