package dev.petuska.kamp.client.view.component

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.core.AttrsBuilder
import dev.petuska.kmdc.core.ContentBuilder
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

object LayoutStyle : StyleSheet() {
  val flexColumn by style {
    display(DisplayStyle.Flex)
    flexDirection(FlexDirection.Column)
  }

  val flexRow by style {
    display(DisplayStyle.Flex)
    flexDirection(FlexDirection.Column)
  }
}

@Composable
fun FlexColumn(
  attrs: AttrsBuilder<HTMLDivElement>? = null,
  content: ContentBuilder<HTMLDivElement>
) {
  Div(
    attrs = {
      classes(LayoutStyle.flexColumn)
      attrs?.invoke(this)
    }
  ) {
    content()
  }
}

@Composable
fun FlexRow(
  attrs: AttrsBuilder<HTMLDivElement>? = null,
  content: ContentBuilder<HTMLDivElement>
) {
  Div(
    attrs = {
      classes(LayoutStyle.flexRow)
      attrs?.invoke(this)
    }
  ) {
    content()
  }
}