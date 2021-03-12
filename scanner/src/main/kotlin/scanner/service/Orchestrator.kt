package scanner.service

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.kodein.di.*
import scanner.domain.*
import scanner.util.*
import kotlin.time.*

class Orchestrator(override val di: DI) : DIAware {
  private val logger by LoggerDelegate()
  private val json by di.instance<Json>()
  
  suspend fun run(scanner: String, cliOptions: CLIOptions? = null) {
    val scannerService = di.direct.instanceOrNull<MavenScannerService<*>>(scanner)
    
    scannerService?.let {
      logger.info("Scanning repository: $scanner")
      
      val duration = measureTime {
        coroutineScope {
          supervisedLaunch {
            logger.info("Starting $scanner scan")
            val count = scanRepo(scannerService, cliOptions)
            logger.info("Found $count kotlin modules with gradle metadata in $scanner repository filtered by ${cliOptions?.include ?: setOf()}, " +
              "explicitly excluding ${cliOptions?.exclude ?: setOf()}")
          }
        }
      }
      logger.info(
        "Finished scanning $scanner in ${
          duration.toComponents { hours, minutes, seconds, nanoseconds ->
            "${hours}h ${minutes}m ${seconds}.${nanoseconds}s"
          }
        }"
      )
    } ?: logger.error("ScannerService for $scanner not found")
  }
  
  private suspend fun scanRepo(scanner: MavenScannerService<*>, cliOptions: CLIOptions? = null): Int {
    var count = 0
    val kamp by di.instance<HttpClient>("kamp")
    scanner.scanKotlinLibraries(cliOptions).buffer().collect { lib ->
      coroutineScope {
        supervisedLaunch {
          count++
          logger.info(json.encodeToString(lib))
          kamp.post<Unit>("${PrivateEnv.API_URL}/api/libraries") {
            body = lib
          }
        }
      }
    }
    kamp.close()
    scanner.close()
    return count
  }
}
