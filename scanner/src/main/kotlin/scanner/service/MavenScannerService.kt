package scanner.service

import kamp.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import scanner.client.*
import scanner.processor.*
import scanner.util.*

abstract class MavenScannerService<A : MavenArtifact>(
  private val pomProcessor: PomProcessor,
  private val gradleModuleProcessor: GradleModuleProcessor,
) {
  protected val logger by LoggerDelegate()
  protected abstract val client: MavenRepositoryClient<A>
  protected abstract val artifacts: Flow<A>
  
  protected open suspend fun buildMppLibrary(
    pomDetails: PomDetails,
    targets: Set<KotlinTarget>,
    artifact: A,
  ): KotlinMPPLibrary? {
    val (description, website, scm) = pomDetails
    
    return KotlinMPPLibrary(
      group = artifact.group,
      name = artifact.name,
      latestVersion = artifact.latestVersion,
      targets = targets,
      description = description,
      website = website,
      scm = scm
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
  
  suspend fun scan(onFound: suspend (KotlinMPPLibrary) -> Unit): Unit = coroutineScope {
    channelFlow {
      artifacts.collect { artifact ->
        client.getGradleModule(artifact)?.also { module ->
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
                buildMppLibrary(pomDetails, targets, artifact)?.also { lib ->
                  send(lib)
                }
              } ?: logger.warn("Could not find pom.xml for module: ${artifact.path}")
            } else {
              logger.info("Non-root gradle module: ${artifact.path}")
            }
          }
        } ?: logger.debug("Not a gradle module: ${artifact.path}")
      }
    }.collect(onFound)
  }
}
