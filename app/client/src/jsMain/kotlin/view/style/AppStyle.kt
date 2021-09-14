package app.client.view.style

import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.display

object AppStyle : StyleSheet() {
  val fixFabContainer by style {
    display(DisplayStyle.Flex)
  }
}
