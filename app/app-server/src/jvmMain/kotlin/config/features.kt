package dev.petuska.kodex.server.config

import dev.petuska.kodex.server.util.PrivateEnv
import dev.petuska.kodex.server.util.PublicEnv
import io.ktor.serialization.kotlinx.cbor.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.ktor.ext.get

fun Application.features() {
  diConfig()
  install(CallLogging) { level = PublicEnv.LOG_LEVEL }
  install(ContentNegotiation) {
    json(this@features.get())
    @OptIn(ExperimentalSerializationApi::class)
    cbor(this@features.get())
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
