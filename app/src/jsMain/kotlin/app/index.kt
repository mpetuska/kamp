package app

import app.config.*
import app.view.*
import io.kvision.*

suspend fun main() {
  loadEnv()
  startApplication(::App, module.hot)
}
