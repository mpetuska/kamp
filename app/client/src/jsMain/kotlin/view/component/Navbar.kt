package app.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.Tag
import androidx.compose.web.attributes.href
import androidx.compose.web.css.AlignItems
import androidx.compose.web.css.DisplayStyle
import androidx.compose.web.css.FlexDirection
import androidx.compose.web.css.JustifyContent
import androidx.compose.web.css.StyleSheet
import androidx.compose.web.css.alignItems
import androidx.compose.web.css.borderRadius
import androidx.compose.web.css.display
import androidx.compose.web.css.flexDirection
import androidx.compose.web.css.height
import androidx.compose.web.css.justifyContent
import androidx.compose.web.css.percent
import androidx.compose.web.css.rem
import androidx.compose.web.css.width
import androidx.compose.web.elements.A
import androidx.compose.web.elements.Img
import androidx.compose.web.elements.Nav
import androidx.compose.web.elements.Text
import app.client.store.AppStore
import app.client.view.style.AppStyle

object NavbarStyle : StyleSheet(AppStyle)

@Composable
fun AppStore.Navbar() {
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
private fun KampIcon(attrs: AttrsBuilder<Tag.Img>.() -> Unit = {}) {
  Img(
    src = "/kamp.svg",
    attrs = attrs,
    style = {
      width(3.rem)
      height(3.rem)
      display(DisplayStyle.Inline)
      borderRadius(50.percent)
    }
  ) {
    Text("kamp icon")
  }
}
