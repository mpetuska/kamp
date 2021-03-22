package scanner.util

import kamp.util.*

object PrivateEnv : Env() {
  val API_URL by EnvDelegate { it ?: "http://localhost:8080" }
  val ADMIN_USER by EnvDelegate { it ?: "admin" }
  val ADMIN_PASSWORD by EnvDelegate { it ?: "admin" }
}
