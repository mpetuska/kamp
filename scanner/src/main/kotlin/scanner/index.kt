package scanner

import kotlinx.coroutines.*
import kotlinx.serialization.*
import scanner.service.*
import scanner.util.*
import java.io.*
import kotlin.time.*

val systemLogger = Logger(System::class)

private suspend fun scanRepo(scanner: ScannerService<*>, outputDirectory: File): Int {
  systemLogger.info { "Starting $scanner scan" }
  var count = 0
  scanner.scan { lib ->
    Logger.writeToFile(File(outputDirectory, "${lib.path.replace(":", "_")}.json")) {
      val str = prettyJson.encodeToString(lib)
      systemLogger.info { str }
      count++
      str
    }
    Logger.appendToFile(File(outputDirectory, "_.json")) {
      "${rawJson.encodeToString(lib)}\n"
    }
  }
  systemLogger.info { "Completed $scanner scan" }
  return count
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
      remotes.map { (name, scanner) ->
        name to async { scanRepo(scanner, outputDirectory.resolve(name)) }
      }.map { (k, v) -> k to v.await() }.forEach { (name, count) ->
        systemLogger.info { "Found $count kotlin modules with gradle metadata in $name repository" }
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
  httpClient.close()
}
