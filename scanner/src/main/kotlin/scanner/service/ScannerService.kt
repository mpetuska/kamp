package scanner.service

import kamp.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.processor.*
import scanner.util.*
import java.io.*
import java.util.concurrent.*

abstract class ScannerService<A : MavenArtifact> {
  protected val logger by Logger()
  protected abstract val client: MavenRepositoryClient<A>
  protected abstract fun CoroutineScope.produceArtifactChannel(): ReceiveChannel<A>
  
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
    val scm: String?
      get() = scmRaw?.let { raw ->
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
    val artifacts = produceArtifactChannel()
    val mppLibs = produce(capacity = 16) {
      parallel {
        artifacts.consumeSafe { artifact ->
          errorSafe {
            client.getGradleModule(artifact)?.also { module ->
              val targets = GradleModuleProcessor.listSupportedTargets(module)
              if (GradleModuleProcessor.isRootModule(module) && targets.isNotEmpty()) {
                client.getMavenPom(artifact)?.let { pom ->
                  val pomDetails = pom.let {
                    PomDetails(
                      PomProcessor.getDescription(pom),
                      PomProcessor.getUrl(pom),
                      PomProcessor.getScmUrl(pom)
                    )
                  }
                  buildMppLibrary(pomDetails, targets, artifact)?.also { lib ->
                    send(lib)
                  }
                } ?: logger.warn { "Could not find pom.xml for module: ${artifact.path}" }
              } else {
                logger.info { "Non-root gradle module: ${artifact.path}" }
              }
            } ?: logger.debug { "Not a gradle module: ${artifact.path}" }
          }
        }
      }
    }
    mppLibs.consumeSafe { lib ->
      onFound(lib)
    }
  }
  
  override fun toString(): String {
    return this::class.simpleName ?: ""
  }
  
  companion object : Closeable {
    protected val coreCount = Runtime.getRuntime().availableProcessors()
    protected val scannerDispatcher: ExecutorCoroutineDispatcher by lazy {
      Executors.newFixedThreadPool(coreCount).asCoroutineDispatcher()
    }
    
    override fun close() {
      scannerDispatcher.close()
    }
    
    @JvmStatic
    protected suspend fun <R> parallel(
      dispatcher: CoroutineDispatcher = scannerDispatcher,
      action: suspend CoroutineScope.() -> R,
    ) = coroutineScope {
      (0 until coreCount).map {
        async(dispatcher) {
          errorSafe { action() }
        }
      }
    }
  }
}
