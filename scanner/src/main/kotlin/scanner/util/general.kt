package scanner.util

import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import org.jsoup.*
import org.jsoup.nodes.*
import scanner.*

val prettyJson = Json {
  prettyPrint = true
}

suspend fun String.asDocument(): Document = withContext(Dispatchers.IO) {
  Jsoup.parse(this@asDocument)
}

suspend inline fun <R> errorSafe(
  action: () -> R,
): R? = runCatching(action).onFailure { e ->
  val stackTrace = (Thread.currentThread().stackTrace + e.stackTrace).joinToString("\n")
  systemLogger.error { "Exception at parallel processor: $e\n${stackTrace}" }
}.getOrNull()
