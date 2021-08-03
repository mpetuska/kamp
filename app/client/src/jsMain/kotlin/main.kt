package app.client

import app.client.config.loadEnv
import app.client.store.appStore
import app.client.view.App
import app.client.view.style.AppStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

actual suspend fun main() {
  loadEnv()
  renderComposable(rootElementId = "root") {
    Style(AppStyle)
    appStore.App()
  }
}
