package app

import app.config.*
import app.util.*
import io.ktor.application.*
import io.ktor.server.cio.*

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
