package dev.petuska.kamp.server

import dev.petuska.kamp.server.config.diConfig
import dev.petuska.kamp.server.config.features
import dev.petuska.kamp.server.config.routing
import dev.petuska.kamp.server.util.PublicEnv
import io.ktor.application.Application
import io.ktor.application.log
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>) {
  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")
  EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
  features()
  routing()
  diConfig()
  log.info("ENV: $PublicEnv")
  log.debug("Full Env: ${System.getenv()}")
}
