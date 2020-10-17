package scanner

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import scanner.service.*
import scanner.util.*
import java.io.*
import kotlin.time.*

val systemLogger = Logger(System::class)

private suspend fun scanRepo(scanner: ScannerService<*>, outputDirectory: File) {
  systemLogger.info { "Starting $scanner scan" }
  scanner.scan { lib ->
    Logger.writeToFile(File(outputDirectory, "${lib.path.replace(":", "_")}.json")) {
      Json {
        prettyPrint = true
      }.encodeToString(lib)
    }
  }
}

@ExperimentalTime
suspend fun main() {
  val outputDirectory = File("out")
  val remotes = mapOf(
    "mavenCentral" to MCScannerService,
    "jCenter" to JCScannerService,
  )
  
  val duration = measureTime {
    coroutineScope {
      remotes.forEach { (name, scanner) ->
        launch { scanRepo(scanner, outputDirectory.resolve(name)) }
      }
    }
  }
  
  systemLogger.info {
    "Finished scanning [${remotes.keys}] in ${
      duration.toComponents { hours, minutes, seconds, nanoseconds ->
        "${hours}h ${minutes}m ${seconds}.${nanoseconds}s"
      }
    }"
  }
  Logger.close()
}
