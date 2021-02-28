package app.config

import app.util.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.slf4j.event.*

fun Application.features() {
  install(CallLogging) {
    level = Level.INFO
  }
  install(StatusPages)
  install(ContentNegotiation) {
    json()
  }
  install(Compression)
  install(DefaultHeaders)
  install(CachingHeaders)
  install(StatusPages)
  install(Authentication) {
    basic {
      validate { credentials ->
        if (credentials.name == PrivateEnv.ADMIN_USER && credentials.password == PrivateEnv.ADMIN_PASSWORD) {
          UserIdPrincipal(credentials.name)
        } else {
          null
        }
      }
    }
  }
}
