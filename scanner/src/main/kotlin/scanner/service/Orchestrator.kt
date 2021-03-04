package scanner.service

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.kodein.di.*
import scanner.util.*
import kotlin.time.*

class Orchestrator(override val di: DI) : DIAware {
  private val logger by LoggerDelegate()
  private val json by di.instance<Json>()
  
  suspend fun run(args: Array<String> = arrayOf()) {
    val scanners = args.map {
      di.direct.instance<MavenScannerService<*>>(it)
    }.map { it::class.qualifiedName to it }.toMap()
    
    logger.info("Scanning repositories: ${scanners.keys}")
    
    val duration = measureTime {
      coroutineScope {
        scanners.mapValues { (_, scanner) -> supervisedAsync { scanRepo(scanner) } }
          .mapValues { (_, it) -> it.await() }
          .forEach { (name, count) ->
            logger.info("Found $count kotlin modules with gradle metadata in $name repository")
          }
      }
    }
    
    logger.info(
      "Finished scanning ${scanners.keys} in ${
        duration.toComponents { hours, minutes, seconds, nanoseconds ->
          "${hours}h ${minutes}m ${seconds}.${nanoseconds}s"
        }
      }"
    )
  }
  
  private suspend fun scanRepo(scanner: MavenScannerService<*>): Int {
    logger.info("Starting $scanner scan")
    var count = 0
    val kamp by di.instance<HttpClient>("kamp")
    scanner.scan().buffer().collect { lib ->
      coroutineScope {
        supervisedLaunch {
          count++
          logger.info(json.encodeToString(lib))
          kamp.post<Unit>("${PrivateEnv.API_URL}/api/libraries") {
            body = lib
          }
        }.join()
      }
    }
    kamp.close()
    scanner.close()
    logger.info("Completed $scanner scan")
    return count
  }
}
