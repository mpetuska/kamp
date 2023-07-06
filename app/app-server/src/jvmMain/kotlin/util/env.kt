package dev.petuska.kodex.server.util

import dev.petuska.kodex.core.util.Env
import org.slf4j.event.Level

object PrivateEnv : Env() {
  val MONGO_STRING by EnvDelegate { it ?: "mongodb://localhost:27017" }
  val MONGO_DATABASE by EnvDelegate { it ?: "kodex" }
  val ADMIN_USER by EnvDelegate { requireNotNull(it) }
  val ADMIN_PASSWORD by EnvDelegate { requireNotNull(it) }
}

object PublicEnv : Env() {
  val LOG_LEVEL by EnvDelegate { it?.uppercase()?.let(Level::valueOf) ?: Level.INFO }
  val PORT by EnvDelegate { it?.toInt() ?: 8080 }
  val API_URL by EnvDelegate {
    it ?: findEnv("WEBSITE_HOSTNAME")?.let { host -> "https://$host" } ?: "http://localhost:$PORT"
  }
}
