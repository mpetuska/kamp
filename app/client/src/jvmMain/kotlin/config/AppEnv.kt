package app.client.config

import kotlinx.serialization.Serializable

@Serializable
actual interface AppEnv {
  actual val API_URL: String
}

private lateinit var _env: AppEnv

actual val env: AppEnv by lazy { _env }

actual suspend fun loadEnv() {
  TODO()
}
