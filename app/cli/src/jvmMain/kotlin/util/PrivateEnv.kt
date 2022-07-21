package dev.petuska.kamp.cli.util

import dev.petuska.kamp.core.util.Env

object PrivateEnv : Env() {
  val MONGO_STRING by EnvDelegate { it ?: "mongodb://localhost:27017" }
  val MONGO_DATABASE by EnvDelegate { it ?: "kamp" }
  val API_URL by EnvDelegate { it ?: "http://localhost:8080" }
  val ADMIN_USER by EnvDelegate { it ?: "admin" }
  val ADMIN_PASSWORD by EnvDelegate { it ?: "admin" }
}
