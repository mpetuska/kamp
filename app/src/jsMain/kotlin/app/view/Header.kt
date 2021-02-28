package app.view

import app.store.*
import app.store.thunk.*
import io.kvision.core.*
import io.kvision.form.*
import io.kvision.form.check.*
import io.kvision.form.range.*
import io.kvision.form.spinner.*
import io.kvision.form.text.*
import io.kvision.html.*
import io.kvision.modal.*
import io.kvision.navbar.*
import io.kvision.state.*
import org.w3c.dom.*


fun Container.Header() = navbar(
  label = "KAMP",
  link = "/",
  type = NavbarType.STICKYTOP,
  nColor = NavbarColor.DARK,
  bgColor = BsBgColor.DARK,
) {
  navForm(rightAlign = true).bind(store) { (libraries, search) ->
    text(type = TextInputType.SEARCH, value = search) {
      placeholder = "Search..."
      onEvent {
        keypress = {
          if (it.charCode == 13) {
            store.dispatch(fetchLibraryPage(page = 1, search = it.target.unsafeCast<HTMLInputElement>().value))
          }
        }
      }
    }
    ul(classes = setOf("pagination", "m-0")) {
      li(classes = setOf("page-item", libraries.prev?.let { "" } ?: "disabled")) {
        tag(type = TAG.A, classes = setOf("page-link")) {
          setAttribute("href", "#")
          span(classes = setOf("fas", "fa-arrow-left"))
          onClick {
            store.dispatch(fetchLibraryPage(libraries.prev))
          }
        }
      }
      li(classes = setOf("page-item")) {
        div(classes = setOf("page-link")) {
          cursor = Cursor.POINTER
          span("${libraries.page}")
          val modal = Modal("Move to page", size = ModalSize.SMALL) {
            val page = spinner(label = "Page", min = 1, value = libraries.page)
            addButton(Button("Go", style = ButtonStyle.OUTLINESUCCESS, type = ButtonType.SUBMIT).apply {
              onClick {
                this@Modal.hide()
                page.value?.toInt()?.let {
                  store.dispatch(fetchLibraryPage(it))
                }
              }
            })
          }
          onClick {
            modal.show()
          }
        }
      }
      li(classes = setOf("page-item", libraries.next?.let { "" } ?: "disabled")) {
        tag(type = TAG.A, classes = setOf("page-link")) {
          setAttribute("href", "#")
          span(classes = setOf("fas", "fa-arrow-right"))
          onClick {
            store.dispatch(fetchLibraryPage(libraries.next))
          }
        }
      }
    }
  }
}
