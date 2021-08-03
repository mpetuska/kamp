package app.client.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.client.store.AppStore
import app.client.store.action.AppAction
import app.client.util.select
import app.client.view.component.FlexColumn
import app.client.view.component.Navbar
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.button.MDCButtonIcon
import dev.petuska.kmdc.button.MDCButtonLabel
import dev.petuska.kmdc.button.MDCButtonOpts
import dev.petuska.kmdc.button.MDCButtonRipple
import dev.petuska.kmdc.icon.button.MDCIconButton
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun AppStore.App() {
  Navbar()
  FlexColumn {

    val count by select { count }
    MDCButton(
      opts = {
        type = MDCButtonOpts.Type.Contained
        icon = MDCButtonOpts.MDCButtonIconType.Trailing
      }
    ) {
      MDCButtonRipple()
      MDCButtonLabel("Contained")
      MDCButtonIcon("favorite")
    }
    MDCIconButton("favorite")
    Div(attrs = { style { padding(25.px) } }) {
    Button(
      attrs = {
        onClick { dispatch(AppAction.DecrementCount) }
      }
    ) {
      Text("-")
    }

    Span(attrs = { style { padding(15.px) } }) {
    Text("$count")
  }

    Button(
      attrs = {
        onClick { dispatch(AppAction.IncrementCount) }
      }
    ) {
      Text("+")
    }
  }
  }
}
