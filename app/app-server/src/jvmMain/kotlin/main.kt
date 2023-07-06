package dev.petuska.kodex.server

import dev.petuska.kodex.server.config.features
import dev.petuska.kodex.server.config.routing
import dev.petuska.kodex.server.util.PublicEnv
import io.ktor.server.application.*
import io.ktor.server.cio.*

fun main(args: Array<String>) {
  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")
  EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
  features()
  routing()
  log.info("ENV: $PublicEnv")
}
