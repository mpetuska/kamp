package dev.petuska.kamp.cli.service

import dev.petuska.kamp.cli.client.MavenRepositoryClient
import dev.petuska.kamp.cli.domain.CLIOptions
import dev.petuska.kamp.cli.processor.GradleModuleProcessor
import dev.petuska.kamp.cli.processor.PomProcessor
import dev.petuska.kamp.cli.util.LoggerDelegate
import dev.petuska.kamp.cli.util.supervisedLaunch
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.MavenArtefact
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow

abstract class MavenScannerService<A : MavenArtefact> : Closeable {
  protected val logger by LoggerDelegate()
  protected abstract val pomProcessor: PomProcessor
  protected abstract val gradleModuleProcessor: GradleModuleProcessor
  protected abstract val client: MavenRepositoryClient<A>
  protected abstract fun CoroutineScope.produceArtifacts(
    cliOptions: CLIOptions? = null,
  ): ReceiveChannel<A>

  private fun CoroutineScope.scanMavenArtefacts(cliOptions: CLIOptions? = null): Flow<A> =
    run {
      logger.info(
        "Scanning from repository root and filtering by ${cliOptions?.include ?: setOf()}, explicitly excluding ${cliOptions?.exclude ?: setOf()}"
      )
      produceArtifacts(cliOptions)
    }
      .receiveAsFlow()

  suspend fun scanKotlinLibraries(cliOptions: CLIOptions? = null): Flow<KotlinLibrary> =
    channelFlow {
      val artefactsFlow = scanMavenArtefacts(cliOptions)

      repeat(Runtime.getRuntime().availableProcessors() * 2) {
        supervisedLaunch {
          artefactsFlow
            .mapNotNull { artefact -> client.getGradleModule(artefact)?.let { artefact to it } }
            .mapNotNull { (artefact, module) ->
              with(gradleModuleProcessor) {
                module.supportedTargets?.takeIf { module.isRootModule && it.isNotEmpty() }?.let {
                  client.getMavenPom(artefact)?.let { pom ->
                    with(pomProcessor) {
                      KotlinLibrary(
                        targets = it,
                        artifact = artefact,
                        description = pom.description,
                        website = pom.url,
                        scm = pom.scmUrl,
                      )
                    }
                  }
                }
              }
            }
            .collect(::send)
        }
      }
    }

  override fun close() = client.close()
}
