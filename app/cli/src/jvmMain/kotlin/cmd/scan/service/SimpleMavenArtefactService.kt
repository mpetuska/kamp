package dev.petuska.kamp.cli.cmd.scan.service

import dev.petuska.kamp.cli.cmd.scan.client.MavenRepositoryClient
import dev.petuska.kamp.cli.cmd.scan.domain.RepositoryItem
import dev.petuska.kamp.cli.cmd.scan.domain.SimpleMavenArtefact
import dev.petuska.kamp.cli.cmd.scan.processor.GradleModuleProcessor
import dev.petuska.kamp.cli.cmd.scan.processor.PomProcessor
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.util.logger
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlin.time.Duration

class SimpleMavenArtefactService(
  private val client: MavenRepositoryClient<SimpleMavenArtefact>,
  private val workers: Int,
  private val delay: Duration?,
) {
  private val logger = logger()

  fun findMavenArtefacts(pages: ReceiveChannel<RepositoryItem.Directory>): Flow<SimpleMavenArtefact> = channelFlow {
    repeat(workers) {
      launch {
        for (page in pages) {
          logger.debug("Looking for maven artefact in $page")
          delay?.let { delay(it) }
          val files = client.listRepositoryPath(page.path)?.filterIsInstance<RepositoryItem.File>()
          val artefact = files?.find { it.name == "maven-metadata.xml" }?.let {
            client.getMavenArtefact(it.path)
          }
          artefact?.let {
            logger.debug("Found maven artefact [$it] in $page")
            send(it)
          }
        }
      }
    }
  }

  fun findKotlinLibraries(artefacts: Flow<SimpleMavenArtefact>): Flow<KotlinLibrary> =
    artefacts.mapNotNull { artefact ->
      with(GradleModuleProcessor()) {
        val module = client.getGradleModule(artefact)?.takeIf { it.isRootModule }
        val supportedTargets = module?.supportedTargets?.takeIf { it.isNotEmpty() }
        supportedTargets?.let {
          client.getMavenPom(artefact)?.let { pom ->
            with(PomProcessor()) {
              KotlinLibrary(
                targets = it,
                artefact = artefact,
                description = pom.description,
                website = pom.url,
                scm = pom.scmUrl,
              )
            }
          }
        }
      }
    }
}
