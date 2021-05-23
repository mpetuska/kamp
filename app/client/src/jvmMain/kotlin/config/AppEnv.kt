package app.client.config

actual interface AppEnv {
  actual val API_URL: String
}

actual val env: AppEnv
  get() = TODO()

actual suspend fun loadEnv() {
  TODO()
}
