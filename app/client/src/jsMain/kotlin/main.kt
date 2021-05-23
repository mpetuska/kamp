package app.client

import androidx.compose.web.css.Style
import androidx.compose.web.renderComposable
import app.client.config.loadEnv
import app.client.store.appStore
import app.client.view.App
import app.client.view.style.AppStyle

actual suspend fun main() {
  loadEnv()
  renderComposable(rootElementId = "root") {
    Style(AppStyle)
    appStore.App()
  }
}
