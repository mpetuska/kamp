package app.client.view.component

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.dom.ContentBuilder
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
  attrs: Builder<AttrsBuilder<HTMLDivElement>>? = null,
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
  attrs: Builder<AttrsBuilder<HTMLDivElement>>? = null,
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
