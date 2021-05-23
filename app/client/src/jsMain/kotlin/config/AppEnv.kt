package app.client.config

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.Window

actual external interface AppEnv {
  actual val API_URL: String
}

inline val Window.env: AppEnv
  get() = asDynamic().env.unsafeCast<AppEnv>()

actual val env: AppEnv
  get() = window.env

actual suspend fun loadEnv() {
  val env: AppEnv = window.fetch("/application.env").await().text().await()
    .split("\n")
    .filter(String::isNotBlank)
    .joinToString(",", "{", "}") {
      val (key, value) = it.split("=", limit = 2).let { c ->
        c[0] to c[1]
      }
      "\"$key\": \"$value\""
    }.let(JSON::parse)
  with(env) {
    requireNotNull(API_URL)
  }
  window.asDynamic().env = env
}
