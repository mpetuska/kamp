package app

import app.config.diConfig
import app.config.features
import app.config.routing
import app.util.PublicEnv
import io.ktor.application.Application
import io.ktor.application.log
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>) {
  System.setProperty("jdk.tls.client.protocols", "TLSv1.2")
  EngineMain.main(args)
}

fun Application.module() {
  features()
  routing()
  diConfig()
  log.info("ENV: $PublicEnv")
  log.debug("Full Env: ${System.getenv()}")
}
