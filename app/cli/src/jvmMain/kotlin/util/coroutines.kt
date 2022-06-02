package dev.petuska.kamp.cli.util

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.*

fun <R> CoroutineScope.supervisedAsync(block: suspend CoroutineScope.() -> R): Deferred<R?> =
  async {
    runCatching {
      supervisorScope(block)
    }.onFailure { e ->
      if ((e !is ClientRequestException) || (e.response.status != HttpStatusCode.NotFound)) {
        e.printStackTrace()
      }
    }.getOrNull()
  }

fun <R> CoroutineScope.supervisedLaunch(block: suspend CoroutineScope.() -> R): Job = launch {
  runCatching {
    supervisorScope(block)
  }.onFailure { it.printStackTrace() }
}
