package app.client.config

expect interface AppEnv {
  val API_URL: String
}

expect val env: AppEnv

expect suspend fun loadEnv()
