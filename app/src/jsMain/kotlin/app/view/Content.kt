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
    color { gray900 }
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
      vertical { "5rem" }
    }
  }) {
    items {
      styled(::h2)({
        textAlign { center }
      }) { +"Kotlin Libraries" }
      gridBox({
        columns(sm = { "1fr" }, md = { repeat(2) { "1fr" } }, xl = { repeat(3) { "1fr" } })
        gap { small }
        width { "max-content" }
        css("align-self: center")
      }) {
        LibraryStore.data.map { it.libraries?.data }.render { libraries ->
          if (libraries == null) {
            spinner({
              size { large }
            }) {
              speed("1s")
            }
          } else {
            for (library in libraries) {
              LibraryCard(library)
            }
          }
        }
      }
    }
  }
}
