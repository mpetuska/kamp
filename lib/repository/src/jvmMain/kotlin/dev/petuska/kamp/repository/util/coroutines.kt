package dev.petuska.kamp.repository.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger

suspend fun <R> runCatchingIO(logger: Logger? = null, block: suspend CoroutineScope.() -> R): Result<R> = runCatching {
  withContext(Dispatchers.IO, block)
}.onFailure { logger?.error(it.message, it) }
