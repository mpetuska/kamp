package app.view

import app.store.*
import app.util.*
import app.view.component.*
import dev.fritz2.components.*
import dev.fritz2.dom.html.*
import kotlinx.coroutines.flow.*


@KampComponent
fun RenderContext.Content() {
  stackUp({
    alignItems { stretch }
    color { dark }
    minHeight { "100%" }
    paddings(
      sm = {
        left { small }
        right { small }
      },
      md = {
        left { larger }
        right { larger }
      },
    )
    margins {
      top { "5rem" }
    }
  }) {
    items {
      styled(::h2)({
        textAlign { center }
      }) { +"Kotlin Libraries" }
      gridBox({
        columns(sm = { "1fr" }, md = { repeat(2) { "1fr" } })
        gap { small }
        width { "max-content" }
        css("align-self: center")
      }) {
        LibraryStore.data.mapNotNull { it.libraries?.data }.render { libraries ->
          for (library in libraries) {
            LibraryCard(library)
          }
        }
      }
    }
  }
}
