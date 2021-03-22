package scanner.testutil

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.jsoup.*

inline fun <reified T> Any.parseJsonFile(path: String) =
  this::class.java.classLoader.getResourceAsStream(path).let {
    Json { ignoreUnknownKeys = true }.decodeFromString<T>(it.reader().use { r -> r.readText() })
  }

fun Any.parseJsonFile(path: String) = parseJsonFile<JsonElement>(path)

fun Any.parseXmlFile(path: String) =
  this::class.java.classLoader.getResourceAsStream(path).let {
    Jsoup.parse(it.reader().use { r -> r.readText() })
  }
