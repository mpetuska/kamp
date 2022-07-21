package dev.petuska.kamp.client.config

@Suppress("PropertyName", "VariableNaming")
interface AppEnv {
  val API_URL: String
  val DEV_MODE: Boolean
}

expect suspend fun loadEnv(vararg args: String): AppEnv
