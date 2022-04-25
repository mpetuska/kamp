package scanner.service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.instanceOrNull
import scanner.domain.CLIOptions
import scanner.util.LoggerDelegate
import scanner.util.PrivateEnv
import scanner.util.supervisedLaunch
import kotlin.time.measureTime

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
            logger.info(
              "Found $count kotlin modules with gradle metadata in $scanner repository filtered by ${cliOptions?.include ?: setOf()}, " +
                "explicitly excluding ${cliOptions?.exclude ?: setOf()}"
            )
          }
        }
      }
      logger.info(
        "Finished scanning $scanner in ${
        duration.toComponents { hours, minutes, seconds, nanoseconds ->
          "${hours}h ${minutes}m $seconds.${nanoseconds}s"
        }
        }"
      )
    }
      ?: logger.error("ScannerService for $scanner not found")
  }

  private suspend fun scanRepo(
    scanner: MavenScannerService<*>,
    cliOptions: CLIOptions? = null,
  ): Int {
    var count = 0
    val kamp by di.instance<HttpClient>("kamp")
    scanner.scanKotlinLibraries(cliOptions).buffer().collect { lib ->
      coroutineScope {
        supervisedLaunch {
          count++
          logger.info("Found lib: ${lib.path}")
          kamp.post<Unit>("${PrivateEnv.API_URL}/api/libraries") { body = lib }
        }
      }
    }
    kamp.close()
    scanner.close()
    return count
  }
}
