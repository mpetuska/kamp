package scanner.service

import io.ktor.utils.io.core.Closeable
import kamp.domain.KotlinMPPLibrary
import kamp.domain.MavenArtifact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import scanner.client.MavenRepositoryClient
import scanner.domain.CLIOptions
import scanner.processor.GradleModuleProcessor
import scanner.processor.PomProcessor
import scanner.util.LoggerDelegate
import scanner.util.supervisedLaunch

abstract class MavenScannerService<A : MavenArtifact> : Closeable {
  protected val logger by LoggerDelegate()
  protected abstract val pomProcessor: PomProcessor
  protected abstract val gradleModuleProcessor: GradleModuleProcessor
  protected abstract val client: MavenRepositoryClient<A>
  protected abstract fun CoroutineScope.produceArtifacts(
    cliOptions: CLIOptions? = null,
  ): ReceiveChannel<A>

  fun CoroutineScope.scanMavenArtefacts(cliOptions: CLIOptions? = null): Flow<A> =
    run {
      logger.info(
        "Scanning from repository root and filtering by ${cliOptions?.include ?: setOf()}, explicitly excluding ${cliOptions?.exclude ?: setOf()}"
      )
      produceArtifacts(cliOptions)
    }
      .receiveAsFlow()

  suspend fun scanKotlinLibraries(cliOptions: CLIOptions? = null): Flow<KotlinMPPLibrary> =
    channelFlow {
      val artefactsFlow = scanMavenArtefacts(cliOptions)

      repeat(Runtime.getRuntime().availableProcessors() * 2) {
        supervisedLaunch {
          artefactsFlow
            .mapNotNull { artefact -> client.getGradleModule(artefact)?.let { artefact to it } }
            .mapNotNull { (artefact, module) ->
              with(gradleModuleProcessor) {
                module.supportedTargets
                  ?.takeIf { module.isRootModule && !it.isNullOrEmpty() }
                  ?.let {
                    client.getMavenPom(artefact)?.let { pom ->
                      with(pomProcessor) {
                        KotlinMPPLibrary(
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
