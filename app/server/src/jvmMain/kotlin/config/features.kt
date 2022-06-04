package dev.petuska.kamp.server.config

import dev.petuska.kamp.server.util.PrivateEnv
import dev.petuska.kamp.server.util.PublicEnv
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
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.features() {
  diConfig()
  val di = closestDI().direct
  install(CallLogging) { level = PublicEnv.LOG_LEVEL }
  install(ContentNegotiation) {
    json(di.instance())
    cbor(di.instance())
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
