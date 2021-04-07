package app.config

import app.util.PrivateEnv
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.basic
import io.ktor.features.CachingHeaders
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.serialization.json
import org.slf4j.event.Level

fun Application.features() {
  install(CallLogging) {
    level = Level.INFO
  }
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
