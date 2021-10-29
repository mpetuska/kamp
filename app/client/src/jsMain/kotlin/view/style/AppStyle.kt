package dev.petuska.kamp.client.view.style

import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.justifyItems

object AppStyle : StyleSheet() {
  val fixFabContainer by style {
    display(DisplayStyle.Flex)
  }

  val centered by style {
    display(DisplayStyle.Grid)
    alignItems(AlignItems.Center)
    justifyItems("center")
  }

  val centeredLeft by style {
    display(DisplayStyle.Grid)
    alignItems(AlignItems.Center)
    justifyItems("left")
  }

  val centeredRight by style {
    display(DisplayStyle.Grid)
    alignItems(AlignItems.Center)
    justifyItems("right")
  }
}
