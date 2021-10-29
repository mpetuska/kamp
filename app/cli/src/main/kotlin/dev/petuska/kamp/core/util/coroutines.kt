package dev.petuska.kamp.cli.util

import io.ktor.client.features.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

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
