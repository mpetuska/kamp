package dev.petuska.kodex.client.config

actual suspend fun loadEnv(vararg args: String): AppEnv {
  // TODO Resolve properly
  return object : AppEnv {
    override val API_URL: String = "https://localhost:8080"
    override val DEV_MODE: Boolean = true
  }
}
