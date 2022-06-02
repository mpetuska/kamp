package dev.petuska.kamp.server.config

import dev.petuska.kamp.server.util.PrivateEnv
import io.ktor.serialization.kotlinx.cbor.cbor
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.statuspages.StatusPages
import org.slf4j.event.Level

fun Application.features() {
  install(CallLogging) { level = Level.INFO }
  install(ContentNegotiation) {
    json()
    cbor()
  }
  install(Compression)
  install(DefaultHeaders)
  install(CachingHeaders)
  install(StatusPages)
  install(Authentication) {
    basic {
      validate { credentials ->
        if (credentials.name == PrivateEnv.ADMIN_USER &&
          credentials.password == PrivateEnv.ADMIN_PASSWORD
        ) {
          UserIdPrincipal(credentials.name)
        } else {
          null
        }
      }
    }
  }
}
