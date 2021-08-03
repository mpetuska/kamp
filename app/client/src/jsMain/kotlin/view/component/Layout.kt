package app.client.view.component

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
inline fun FlexColumn(crossinline content: ContentBuilder<HTMLDivElement>) {
  Div(
    attrs = {
      style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
      }
    }
  ) {
    content()
  }
}

@Composable
inline fun FlexRow(crossinline content: ContentBuilder<HTMLDivElement>) {
  Div(
    attrs = {
      style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Row)
      }
    }
  ) {
    content()
  }
}
