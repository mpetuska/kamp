package app.client.view.style

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color

interface Colours {
  val primary: CSSColorValue
  val secondary: CSSColorValue
  val success: CSSColorValue
  val danger: CSSColorValue
  val warning: CSSColorValue
  val info: CSSColorValue
  val light: CSSColorValue
  val dark: CSSColorValue
}

object LightColours : Colours {
  override val primary: CSSColorValue = Color("#2B3F4F")
  override val secondary: CSSColorValue = Color("#95A5A6")
  override val success: CSSColorValue = Color("#00BE9D")
  override val danger: CSSColorValue = Color("#E94241")
  override val warning: CSSColorValue = Color("#F4982F")
  override val info: CSSColorValue = Color("#2E9BD8")
  override val light: CSSColorValue = Color("#ECF0F1")
  override val dark: CSSColorValue = Color("#7B8A8B")
}

object DarkColours : Colours {
  override val primary: CSSColorValue = Color("#365B7D")
  override val secondary: CSSColorValue = Color("#444444")
  override val success: CSSColorValue = Color("#00BD8E")
  override val danger: CSSColorValue = Color("#E94241")
  override val warning: CSSColorValue = Color("#F4982F")
  override val info: CSSColorValue = Color("#2E9BD8")
  override val light: CSSColorValue = Color("#ADB5BD")
  override val dark: CSSColorValue = Color("#303030")
}
