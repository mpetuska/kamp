package app.config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*

fun Application.features() {
  install(CallLogging)
  install(ContentNegotiation) {
    json()
  }
  install(Compression)
  install(DefaultHeaders)
  install(CachingHeaders)
  install(StatusPages)
}
