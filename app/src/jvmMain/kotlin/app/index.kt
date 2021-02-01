package app

import app.config.*
import app.util.*
import io.ktor.application.*
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
  features()
  routing()
  dependencyInjection()
  log.info("ENV: $Env")
  log.info("Full Env: ${System.getenv()}")
}
