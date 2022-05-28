package dev.petuska.kamp.cli.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

val rawJson = Json { ignoreUnknownKeys = true }
val prettyJson = Json {
  prettyPrint = true
  ignoreUnknownKeys = true
}

suspend fun String.asDocument(): Document =
  withContext(Dispatchers.IO) { Jsoup.parse(this@asDocument) }
