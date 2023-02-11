package dev.petuska.kodex.client.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

inline fun <T1, T2> suspending(
  crossinline block: suspend CoroutineScope.(T1, T2) -> Unit
): (T1, T2) -> Unit = { t1, t2 -> suspending { block(t1, t2) } }

inline fun <T1> suspending(
  crossinline block: suspend CoroutineScope.(T1) -> Unit
): (T1) -> Unit = { suspending { block(it) } }

inline fun suspending(
  crossinline block: suspend CoroutineScope.() -> Unit
) {
  CoroutineScope(EmptyCoroutineContext).launch { block() }
}
