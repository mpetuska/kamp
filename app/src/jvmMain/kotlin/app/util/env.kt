package app.util

import kamp.util.*


object PrivateEnv : Env() {
  val MONGO_STRING by EnvDelegate { it ?: "mongodb://localhost:27017" }
  val MONGO_DATABASE by EnvDelegate { it ?: "kamp" }
  val SEARCH_SERVICE_KEY by EnvDelegate { it }
  val SEARCH_SERVICE_URL by EnvDelegate { it }
  val ADMIN_USER by EnvDelegate { requireNotNull(it) }
  val ADMIN_PASSWORD by EnvDelegate { requireNotNull(it) }
}

object PublicEnv : Env() {
  val API_URL by EnvDelegate {
    it ?: findEnv("WEBSITE_HOSTNAME")?.let { host -> "https://$host" } ?: "http://localhost:8080"
  }
}
