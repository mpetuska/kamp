package app.client.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

inline fun <T1, T2> suspending(crossinline block: suspend CoroutineScope.(T1, T2) -> Unit): (T1, T2) -> Unit =
  { t1, t2 -> suspending { block(t1, t2) } }

inline fun <T1> suspending(crossinline block: suspend CoroutineScope.(T1) -> Unit): (T1) -> Unit =
  { suspending { block(it) } }

inline fun suspending(crossinline block: suspend CoroutineScope.() -> Unit) {
  GlobalScope.launch { block() }
}
