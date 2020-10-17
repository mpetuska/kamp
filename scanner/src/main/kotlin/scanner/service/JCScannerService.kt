package scanner.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.domain.jc.*
import scanner.util.*

object JCScannerService : ScannerService<JCArtifact>() {
  override val client = JCenterClient
  private const val pageCount = 100000 //49647
  private const val endPage = pageCount
  private const val startPage = 0 //kodein 5863
  
  override fun CoroutineScope.produceArtifactChannel(): ReceiveChannel<JCArtifact> =
    produce {
      val pages = produce {
        (startPage..endPage).forEach { send(it) }
      }
      val packages = produce {
        parallel {
          for (page in pages) {
            logger.debug { "Scanning JC page $page" }
            val done = errorSafe {
              JCenterClient.getPackages(page)?.also { packages ->
                packages.forEach { send(it) }
              } == null
            } == true
            if (done) break
          }
        }
      }
      parallel {
        for (pkg in packages) {
          errorSafe {
            JCenterClient.getPackageArtifacts(pkg)?.let { artifacts ->
              artifacts.forEach { send(it) }
            }
          }
        }
      }
    }
}
