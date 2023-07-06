package dev.petuska.kodex.client.view.style

import org.jetbrains.compose.web.css.*

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
