package dev.petuska.kodex.client

import dev.petuska.kodex.client.config.loadEnv
import dev.petuska.kodex.client.view.App
import dev.petuska.kodex.client.view.KodexApp
import dev.petuska.kodex.client.view.style.AppStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

@JsModule("./style/index.scss")
private external val Style: dynamic

suspend fun main(vararg args: String) {
  val env = loadEnv(args = args)
  Style
  renderComposable(rootElementId = "root") {
    Style(AppStyle)
    KodexApp(env) { App() }
  }
}
