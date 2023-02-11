package dev.petuska.kodex.cli.testutil

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.jsoup.Jsoup

val laxJson = Json { ignoreUnknownKeys = true }

inline fun <reified T> Any.parseJsonFile(path: String) =
  this::class.java.classLoader.getResourceAsStream(path)?.let {
    laxJson.decodeFromString<T>(it.reader().use { r -> r.readText() })
  } ?: error("Resource $path not found")

fun Any.parseJsonFile(path: String) = parseJsonFile<JsonElement>(path)

fun Any.parseXmlFile(path: String) =
  this::class.java.classLoader.getResourceAsStream(path)?.let {
    Jsoup.parse(it.reader().use { r -> r.readText() })
  } ?: error("Resource $path not found")
