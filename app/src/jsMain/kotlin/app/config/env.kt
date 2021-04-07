package app.config

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.Window

external interface AppEnv {
  val API_URL: String
}

inline val Window.env: AppEnv
  get() = asDynamic().env.unsafeCast<AppEnv>()

suspend fun loadEnv(): AppEnv {
  val env: AppEnv = window.fetch("/application.env").await().text().await()
    .split("\n")
    .filter(String::isNotBlank)
    .joinToString(",", "{", "}") {
      val (key, value) = it.split("=", limit = 2).let { c ->
        c[0] to c[1]
      }
      "\"$key\": \"$value\""
    }.let(JSON::parse)
  window.asDynamic().env = env
  requireNotNull(window.env.API_URL)
  return env
}
