package scanner.util

import kotlinx.coroutines.*

fun <R> CoroutineScope.asyncOrNull(block: suspend CoroutineScope.() -> R): Deferred<R?> = async {
  try {
    supervisorScope(block)
  } catch (e: Exception) {
    null
  }
}
