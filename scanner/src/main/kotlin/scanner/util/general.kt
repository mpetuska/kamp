package scanner.util

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.serialization.json.*
import org.jsoup.*
import org.jsoup.nodes.*
import scanner.*

val rawJson = Json { ignoreUnknownKeys = true }
val prettyJson = Json {
  prettyPrint = true
  ignoreUnknownKeys = true
}

suspend fun String.asDocument(): Document =
    withContext(Dispatchers.IO) { Jsoup.parse(this@asDocument) }
