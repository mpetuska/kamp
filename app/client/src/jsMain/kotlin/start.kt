package app.client

import app.client.view.App
import app.client.view.style.AppStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

actual suspend fun AppContext.start() {
  renderComposable(rootElementId = "root") {
    Style(AppStyle)
    App()
  }
}
