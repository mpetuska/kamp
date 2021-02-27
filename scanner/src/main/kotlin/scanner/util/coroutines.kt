package scanner.util

import kotlinx.coroutines.*

fun <R> CoroutineScope.supervisedAsync(block: suspend CoroutineScope.() -> R): Deferred<R?> = async {
  try {
    supervisorScope(block)
  } catch (e: Exception) {
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
