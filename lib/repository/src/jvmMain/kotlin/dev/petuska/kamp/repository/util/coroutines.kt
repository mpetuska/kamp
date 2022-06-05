package dev.petuska.kamp.repository.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <R> runCatchingIO(block: suspend CoroutineScope.() -> R): Result<R> = runCatching {
  withContext(Dispatchers.IO, block)
}.onFailure { it.printStackTrace() }
