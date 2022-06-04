package dev.petuska.kamp.server

import dev.petuska.kamp.core.di.ApplicationDIScope
import dev.petuska.kamp.server.config.features
import dev.petuska.kamp.server.config.routing
import dev.petuska.kamp.server.util.PublicEnv
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopPreparing
import io.ktor.server.application.log
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>) {
  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")
  EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
  features()
  routing()
  log.info("ENV: $PublicEnv")
  log.debug("Full Env: ${System.getenv()}")
  environment.monitor.subscribe(ApplicationStopPreparing) {
    ApplicationDIScope.close()
  }
}
