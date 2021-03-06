package app.view

import app.store.*
import app.util.*
import app.view.component.*
import dev.fritz2.binding.*
import dev.fritz2.components.*
import dev.fritz2.dom.html.*
import dev.fritz2.styling.params.*
import kotlinx.browser.*
import kotlinx.coroutines.flow.*


//fun Container.Header() = navbar(
//  label = "KAMP",
//  link = "/",
//  type = NavbarType.STICKYTOP,
//  nColor = NavbarColor.DARK,
//  bgColor = BsBgColor.DARK,
//).bind(store) { state ->
//  state.libraryCount?.let {
//    span(classes = setOf("badge", "badge-pill", "badge-info", "mr-2")) {
//      +"$it Libraries"
//    }
//  }
//  navForm(rightAlign = true).bind(store) { (libraries, search) ->
//    text(type = TextInputType.SEARCH, value = search) {
//      addCssClass("m-0")
//      addCssClass("mr-1")
//      placeholder = "Search..."
//      onEvent {
//        keypress = {
//          if (it.charCode == 13) {
//            store.dispatch(fetchLibraryPage(page = 1, search = it.target.unsafeCast<HTMLInputElement>().value))
//          }
//        }
//      }
//    }
//    ul(classes = setOf("pagination", "m-0")) {
//      li(classes = setOf("page-item", libraries.prev?.let { "" } ?: "disabled")) {
//        tag(type = TAG.A, classes = setOf("page-link")) {
//          setAttribute("href", "#")
//          span(classes = setOf("fas", "fa-arrow-left"))
//          onClick {
//            store.dispatch(fetchLibraryPage(libraries.prev))
//          }
//        }
//      }
//      li(classes = setOf("page-item")) {
//        div(classes = setOf("page-link")) {
//          cursor = Cursor.POINTER
//          span("${libraries.page}")
//          val modal = Modal("Move to page", size = ModalSize.SMALL) {
//            val page = spinner(label = "Page", min = 1, value = libraries.page)
//            addButton(Button("Go", style = ButtonStyle.OUTLINESUCCESS, type = ButtonType.SUBMIT).apply {
//              onClick {
//                this@Modal.hide()
//                page.value?.toInt()?.let {
//                  store.dispatch(fetchLibraryPage(it))
//                }
//              }
//            })
//          }
//          onClick {
//            modal.show()
//          }
//        }
//      }
//      li(classes = setOf("page-item", libraries.next?.let { "" } ?: "disabled")) {
//        tag(type = TAG.A, classes = setOf("page-link")) {
//          setAttribute("href", "#")
//          span(classes = setOf("fas", "fa-arrow-right"))
//          onClick {
//            store.dispatch(fetchLibraryPage(libraries.next))
//          }
//        }
//      }
//    }
//  }
//}

@KampComponent
fun RenderContext.Header() {
  val menuStore = storeOf(false)
  
  styled(::div)({
    children("&[data-menu-open] #menu-bottom") {
      display { flex }
    }
  }) {
    attr("data-menu-open", menuStore.data)
    
    navBar({
      border { width { "0" } }
      boxShadow { flat }
    }) {
      brand {
        (::a.styled {
          after {
            textAlign { center }
            background { color { primary } }
            color { lightGray }
          }
          alignItems { end }
        }) {
          href("/")
          KampIcon {
            size { "3rem" }
            color { primary }
            display { inline }
            css("border-radius: 50%")
          }
          
          styled(::span)({
            margins { left { smaller } }
            verticalAlign { sub }
            fontSize(sm = { large }, md = { larger })
            fontWeight { lighter }
          }) { +"KAMP" }
        }
        LibraryStore.data.mapNotNull { it.count }.render { count ->
          Badge("$count Libraries", { success }) {
            margins {
              left { small }
            }
          }
        }
      }
      
      actions {
        lineUp({
          display(sm = { none }, md = { flex })
        }, id = "menu-bottom") {
          items {
            NavAnchor("https://github.com/mpetuska/kamp", "_new") {
              +"GitHub"
            }
          }
        }
        clickButton({
          display(sm = { flex }, md = { none })
        }) {
          icon { fromTheme { menu } }
        }.map {
          window.scrollTo(0.0, 0.0)
          !menuStore.current
        } handledBy menuStore.update
      }
    }
  }
}
