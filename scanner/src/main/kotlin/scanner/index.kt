package scanner

import kotlinx.coroutines.*
import kotlinx.serialization.*
import scanner.service.*
import scanner.util.*
import java.io.*
import kotlin.time.*

val systemLogger = Logger(System::class)

private suspend fun scanRepo(scanner: ScannerService<*>, outputDirectory: File) {
  systemLogger.info { "Starting $scanner scan" }
  scanner.scan { lib ->
    Logger.writeToFile(File(outputDirectory, "${lib.path.replace(":", "_")}.json")) {
      val str = prettyJson.encodeToString(lib)
      systemLogger.info { str }
      str
    }
  }
}

@ExperimentalTime
suspend fun main(args: Array<String>) {
  val outputDirectory = File("out")
  val remotes = mapOf(
    "mavenCentral" to MCScannerService,
    "jCenter" to JCScannerService,
  ).filterKeys {
    args.isEmpty() || args.any { arg -> arg.equals(it, true) }
  }
  
  systemLogger.info { "Scanning ${remotes.keys} repositories" }
  
  val duration = measureTime {
    coroutineScope {
      remotes.forEach { (name, scanner) ->
        launch { scanRepo(scanner, outputDirectory.resolve(name)) }
      }
    }
  }
  
  systemLogger.info {
    "Finished scanning ${remotes.keys} in ${
      duration.toComponents { hours, minutes, seconds, nanoseconds ->
        "${hours}h ${minutes}m ${seconds}.${nanoseconds}s"
      }
    }"
  }
  Logger.close()
  ScannerService.close()
}
