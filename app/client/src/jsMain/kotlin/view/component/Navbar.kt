package app.client.view.component

import androidx.compose.runtime.Composable
import app.client.AppContext
import app.client.view.style.AppStyle
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLImageElement

object NavbarStyle : StyleSheet(AppStyle)

@Composable
fun AppContext.Navbar() {
  Nav(
    attrs = {
      classes(
        NavbarStyle.css {
          display(DisplayStyle.Flex)
          flexDirection(FlexDirection.Row)
          justifyContent(JustifyContent.SpaceBetween)
          alignItems(AlignItems.Center)
        }
      )
    }
  ) {

    A(
      attrs = {
        href("#!")
        classes(
          NavbarStyle.css {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            alignItems(AlignItems.Center)
          }
        )
      }
    ) {
      KampIcon()
      Text("KAMP")
    }
  }
}

@Composable
private fun KampIcon(attrs: AttrsBuilder<HTMLImageElement>.() -> Unit = {}) {
  Img(
    src = "/kamp.svg",
    attrs = {
      attrs()
      style {
        width(3.cssRem)
        height(3.cssRem)
        display(DisplayStyle.Inline)
        borderRadius(50.percent)
      }
    },
  )
}
