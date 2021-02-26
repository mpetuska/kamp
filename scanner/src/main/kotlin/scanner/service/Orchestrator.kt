package scanner.service

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.kodein.di.*
import scanner.util.*
import kotlin.time.*

class Orchestrator(override val di: DI) : DIAware {
  private val logger by LoggerDelegate()
  
  suspend fun run(args: Array<String> = arrayOf()) {
    val scanners = args.map {
      di.direct.instance<MavenScannerService<*>>(it)
    }.map { it::class.qualifiedName to it }.toMap()
    
    logger.info("Scanning repositories: ${scanners.keys}")
    
    val duration = measureTime {
      coroutineScope {
        scanners.mapValues { (_, scanner) -> asyncOrNull { scanRepo(scanner) } }
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
    val count = MutableStateFlow(0)
    scanner.scan { lib ->
      count.value++
      val kamp by di.instance<HttpClient>()
      kamp.use {
        it.post("${PrivateEnv.API_URL}/api/library") {
          body = lib
        }
      }
    }
    logger.info("Completed $scanner scan")
    return count.value
  }
}
