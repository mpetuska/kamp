package app.util

import kotlinx.coroutines.*

external fun require(module: String): dynamic

inline fun <T> suspending(crossinline block: suspend CoroutineScope.(T) -> Unit): (T) -> Unit =
  { GlobalScope.launch { block(it) } }

inline fun suspending(crossinline block: suspend CoroutineScope.() -> Unit) {
  GlobalScope.launch { block() }
}
