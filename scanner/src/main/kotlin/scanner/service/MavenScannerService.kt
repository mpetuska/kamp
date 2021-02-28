package scanner.service

import io.ktor.utils.io.core.*
import kamp.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import scanner.client.*
import scanner.processor.*
import scanner.util.*

abstract class MavenScannerService<A : MavenArtifact> : Closeable {
  protected val logger by LoggerDelegate()
  protected abstract val pomProcessor: PomProcessor
  protected abstract val gradleModuleProcessor: GradleModuleProcessor
  protected abstract val client: MavenRepositoryClient<A>
  protected abstract fun CoroutineScope.produceArtifacts(): ReceiveChannel<A>
  
  private fun buildMppLibrary(
    pomDetails: PomDetails,
    targets: Set<KotlinTarget>,
    artifact: A,
  ): KotlinMPPLibrary {
    val (description, website, scm) = pomDetails
    
    return KotlinMPPLibrary(
      artifact = artifact,
      targets = targets,
      description = description,
      website = website,
      scm = scm,
    )
  }
  
  protected class PomDetails(val description: String?, val website: String?, scm: String?) {
    private val scmRaw = scm
    val scm: String? = scmRaw?.let { raw ->
      var res = raw.removeSuffix("/")
      if (!res.endsWith(".git")) {
        res += ".git"
      }
      val chunks = res.split("//")
      if (chunks.size == 2) {
        res = "https://${chunks[1]}"
      }
      res
    }
  
    operator fun component1() = description
    operator fun component2() = website
    operator fun component3() = scm
  }
  
  suspend fun scan() = channelFlow {
    val artifactsChannel = produceArtifacts()
    
    List(Runtime.getRuntime().availableProcessors() * 2) {
      supervisedLaunch {
        for (artifact in artifactsChannel) {
          client.getGradleModule(artifact)?.let { module ->
            with(gradleModuleProcessor) {
              val targets = module.supportedTargets
              if (module.isRootModule && !targets.isNullOrEmpty()) {
                client.getMavenPom(artifact)?.let { pom ->
                  val pomDetails = with(pomProcessor) {
                    PomDetails(
                      description = pom.description,
                      website = pom.url,
                      scm = pom.scmUrl,
                    )
                  }
                  send(buildMppLibrary(pomDetails, targets, artifact))
                } ?: logger.warn("Could not find pom.xml for module: ${artifact.path}")
              } else {
                logger.debug("Non-root gradle module: ${artifact.path}")
              }
            }
          } ?: logger.debug("Not a gradle module: ${artifact.path}")
        }
      }
    }.joinAll()
  }
  
  override fun close() = client.close()
}
