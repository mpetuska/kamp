package dev.petuska.kamp.client.config

@Suppress("PropertyName")
interface AppEnv {
  val API_URL: String
  val DEV_MODE: Boolean
}

expect suspend fun loadEnv(args: Set<String>): AppEnv
