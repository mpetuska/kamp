package dev.petuska.kamp.client.config

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlin.js.Json

actual suspend fun loadEnv(args: Set<String>): AppEnv {
  val envJson: Json = window.fetch("/application.env").await().text().await()
    .split("\n")
    .filter(String::isNotBlank)
    .joinToString(",", "{", "}") {
      val (key, value) = it.split("=", limit = 2).let { c ->
        c[0] to c[1]
      }
      "\"$key\": \"$value\""
    }.let(JSON::parse)
  val env = object : AppEnv {
    override val API_URL: String = envJson["API_URL"]!!.toString()
    override val DEV_MODE: Boolean = envJson["DEV_MODE"]?.let { !"$it".equals("false", ignoreCase = true) } ?: false
  }
  window.asDynamic().env = env
  return env
}
