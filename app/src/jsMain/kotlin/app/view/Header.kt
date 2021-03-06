package app.view

import app.store.*
import app.util.*
import app.view.component.*
import dev.fritz2.binding.*
import dev.fritz2.components.*
import dev.fritz2.dom.html.*
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
private fun RenderContext.SearchModal() = modal { close ->
  val searchStore = storeOf("")
  val targetsStore = storeOf(listOf<String>())
  content {
    stackUp {
      formControl {
        inputField(store = searchStore) {
          placeholder("Search...")
        }
      }
      formControl {
        label("Targets:")
        
        checkboxGroup(
          items = listOf("jvm", "android", "js"),
          store = targetsStore
        ) {
          direction { column }
        }
      }
      clickButton {
        text("Search")
        icon { fromTheme { search } }
      } handledBy close
    }
  }
}

@KampComponent
fun RenderContext.Header() {
  styled(::div)({
    children("&[data-menu-open] #menu-bottom") {
      display { flex }
    }
  }) {
    navBar({
      border { width { "0" } }
      boxShadow { flat }
    }) {
      brand {
        styled(::a)({
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
        LibraryStore.data.map { it.count }.render { count ->
          Badge({ success }, {
            margins {
              left { small }
            }
          }) {
            if (count != null) {
              +"$count Libraries"
            } else {
              spinner {
                speed("1s")
              }
            }
          }
        }
      }
      
      actions {
        lineUp({
          display(sm = { none }, md = { flex })
        }) {
          items {
            NavAnchor("https://github.com/mpetuska/kamp", "_new") {
              +"GitHub"
            }
          }
        }
        val modal = SearchModal()
        clickButton({
          display(sm = { inlineBlock }, md = { none })
        }) {
          icon { fromTheme { search } }
        } handledBy modal
        clickButton({
          display(sm = { none }, md = { inlineBlock })
        }) {
          text("Search")
          icon { fromTheme { search } }
        } handledBy modal
      }
    }
  }
}
