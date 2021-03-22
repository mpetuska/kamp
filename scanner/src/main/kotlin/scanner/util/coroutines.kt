package scanner.util

import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.coroutines.*

fun <R> CoroutineScope.supervisedAsync(block: suspend CoroutineScope.() -> R): Deferred<R?> =
    async {
  try {
    supervisorScope(block)
  } catch (e: Exception) {
    if (e !is ClientRequestException || e.response.status != HttpStatusCode.NotFound) {
      e.printStackTrace()
    }
    null
  }
}

fun <R> CoroutineScope.supervisedLaunch(block: suspend CoroutineScope.() -> R): Job = launch {
  try {
    supervisorScope(block)
  } catch (e: Exception) {
    e.printStackTrace()
  }
}
