package scanner.util

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.serialization.json.*
import org.jsoup.*
import org.jsoup.nodes.*
import scanner.*

val rawJson = Json {
  ignoreUnknownKeys = true
}
val prettyJson = Json {
  prettyPrint = true
  ignoreUnknownKeys = true
}

suspend fun String.asDocument(): Document = withContext(Dispatchers.IO) {
  Jsoup.parse(this@asDocument)
}

suspend fun <T> ReceiveChannel<T>.consumeSafe(action: suspend (T) -> Unit) = consumeSafeBreaking {
  action(it)
  false
}

suspend fun <T> ReceiveChannel<T>.consumeSafeBreaking(action: suspend (T) -> Boolean) {
  coroutineScope {
    var done = false
    do {
      runCatching {
        for (it in this@consumeSafeBreaking) {
          done = action(it)
          if (done) break
        }
      }.onFailure { e ->
        systemLogger.error { "Exception at channel consumer: $e\n${e.stackTraceToString()}" }
      }
    } while (!isClosedForReceive && !done)
  }
}

suspend fun <R> errorSafe(
  action: suspend () -> R,
): R? = runCatching { action() }.onFailure { e ->
  val stackTrace = (Thread.currentThread().stackTrace + e.stackTrace).joinToString("\n")
  systemLogger.error { "Exception at parallel processor: $e\n${stackTrace}" }
}.getOrNull()
