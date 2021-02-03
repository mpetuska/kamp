package app

import app.component.*
import app.config.*
import kotlinx.browser.*
import react.dom.*

suspend fun main() {
  loadEnv()
  render(document.getElementById("root")) {
    App {}
  }
}
