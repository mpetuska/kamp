package app.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.web.css.DisplayStyle
import androidx.compose.web.css.FlexDirection
import androidx.compose.web.css.display
import androidx.compose.web.css.flexDirection
import androidx.compose.web.elements.Div

@Composable
inline fun FlexColumn(content: @Composable () -> Unit) {
  Div(
    style = {
      display(DisplayStyle.Flex)
      flexDirection(FlexDirection.Column)
    }
  ) {
    content()
  }
}

@Composable
inline fun FlexRow(content: @Composable () -> Unit) {
  Div(
    style = {
      display(DisplayStyle.Flex)
      flexDirection(FlexDirection.Row)
    }
  ) {
    content()
  }
}
