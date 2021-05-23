package app.client.view.style

import androidx.compose.web.css.Color

interface Colours {
  val primary: Color
  val secondary: Color
  val success: Color
  val danger: Color
  val warning: Color
  val info: Color
  val light: Color
  val dark: Color
}

object LightColours : Colours {
  override val primary: Color = Color("#2B3F4F")
  override val secondary: Color = Color("#95A5A6")
  override val success: Color = Color("#00BE9D")
  override val danger: Color = Color("#E94241")
  override val warning: Color = Color("#F4982F")
  override val info: Color = Color("#2E9BD8")
  override val light: Color = Color("#ECF0F1")
  override val dark: Color = Color("#7B8A8B")
}

object DarkColours : Colours {
  override val primary: Color = Color("#365B7D")
  override val secondary: Color = Color("#444444")
  override val success: Color = Color("#00BD8E")
  override val danger: Color = Color("#E94241")
  override val warning: Color = Color("#F4982F")
  override val info: Color = Color("#2E9BD8")
  override val light: Color = Color("#ADB5BD")
  override val dark: Color = Color("#303030")
}
