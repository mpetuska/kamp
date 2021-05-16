package app.client.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.web.css.padding
import androidx.compose.web.css.px
import androidx.compose.web.elements.Button
import androidx.compose.web.elements.Div
import androidx.compose.web.elements.Span
import androidx.compose.web.elements.Text
import app.client.store.AppStore
import app.client.store.action.AppAction
import app.client.util.select

@Composable
fun App() {
  val count by AppStore.select { count }
  Div(style = { padding(25.px) }) {
    Button(
      attrs = {
        onClick { AppStore.dispatch(AppAction.DecrementCount) }
      }
    ) {
      Text("-")
    }

    Span(style = { padding(15.px) }) {
      Text("$count")
    }

    Button(
      attrs = {
        onClick { AppStore.dispatch(AppAction.IncrementCount) }
      }
    ) {
      Text("+")
    }
  }
}
