package app.client

import androidx.compose.web.renderComposable
import app.client.config.loadEnv
import app.client.view.App

actual suspend fun main() {
  loadEnv()
  renderComposable(rootElementId = "root") {
    App()
  }
}
