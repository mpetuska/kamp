package app.view

import app.store.*
import app.store.thunk.*
import io.kvision.core.*
import io.kvision.html.*
import io.kvision.navbar.*
import io.kvision.state.*


fun Container.Header() = navbar(
  label = "KAMP",
  link = "/",
  type = NavbarType.STICKYTOP,
  nColor = NavbarColor.DARK,
  bgColor = BsBgColor.DARK,
) {
  nav(rightAlign = true).bind(store) { (libraries) ->
    button(
      "Previous",
      disabled = libraries.prev == null,
      labelFirst = false,
      style = ButtonStyle.OUTLINEINFO,
      classes = setOf("mr-2")
    ) {
      onClick {
        store.dispatch(fetchLibraryPage(libraries.prev))
      }
      span(classes = setOf("mr-2", "fas", "fa-arrow-left"))
    }
    button(
      "Next",
      disabled = libraries.next == null,
      labelFirst = true,
      style = ButtonStyle.OUTLINEINFO,
    ) {
      onClick {
        store.dispatch(fetchLibraryPage(libraries.next))
      }
      span(classes = setOf("ml-2", "fas", "fa-arrow-right"))
    }
  }
}
