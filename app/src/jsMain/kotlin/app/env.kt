package app

import io.ktor.client.fetch.*
import kotlinx.browser.*
import kotlinx.coroutines.*
import org.w3c.dom.*

external interface AppEnv {
  val API_URL: String
}

inline val Window.env: AppEnv
  get() = asDynamic().env.unsafeCast<AppEnv>()

fun loadEnv() = GlobalScope.async {
  @Suppress("UNUSED_VARIABLE")
  val env: AppEnv = fetch("/application.env").await().text().await()
    .split("\n")
    .filter(String::isNotBlank)
    .joinToString(",", "{", "}") {
      val (key, value) = it.split("=", limit = 2).let { c ->
        c[0] to c[1]
      }
      "\"$key\": \"$value\""
    }.let(JSON::parse)
  js("window.env = env")
  requireNotNull(window.env.API_URL)
}
