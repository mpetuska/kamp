package dev.petuska.kamp.cli.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.time.Duration

suspend fun String.asDocument(): Document =
  withContext(Dispatchers.IO) { Jsoup.parse(this@asDocument) }

fun Duration.toHumanString() = toComponents { hours, minutes, seconds, nanoseconds ->
  "${hours}h ${minutes}m $seconds.${nanoseconds}s"
}
