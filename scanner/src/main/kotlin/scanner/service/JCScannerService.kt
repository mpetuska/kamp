package scanner.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.domain.jc.*
import scanner.util.*
import java.io.*

object JCScannerService : ScannerService<JCArtifact>() {
  override val client = JCenterClient
  private const val startPage = 0 //kodein 5863
  
  override fun CoroutineScope.produceArtifactChannel(): ReceiveChannel<JCArtifact> =
    produce(capacity = 16) {
      JCenterClient.getPageCount()?.let { pageCount ->
        val pages = produce(capacity = 16) {
          (startPage until pageCount).forEach { send(it) }
        }
        val packages = produce(capacity = 16) {
          parallel {
            pages.consumeSafeBreaking { page ->
              logger.info { "Scanning JC page [$page/$pageCount]" }
              val done = errorSafe {
                JCenterClient.getPackages(page)?.also { packages ->
                  packages.forEach {
                    Logger.appendToFile(File("out/jc.log")) {
                      "$it\n"
                    }
                    send(it)
                  }
                } == null
              } == true
              done
            }
          }.joinAll()
          pages.cancel()
        }
        parallel {
          packages.consumeSafe { pkg ->
            val packageArtifacts = errorSafe {
              JCenterClient.getPackageArtifacts(pkg)
            }
            packageArtifacts?.forEach { send(it) }
          }
        }
      }
    }
}
