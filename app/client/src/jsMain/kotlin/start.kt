package dev.petuska.kamp.client

import dev.petuska.kamp.client.view.App
import dev.petuska.kamp.client.view.KampApp
import dev.petuska.kamp.client.view.style.AppStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

actual suspend fun AppContext.start() {
  renderComposable(rootElementId = "root") {
    Style(AppStyle)
    KampApp(di) { App() }
  }
}
