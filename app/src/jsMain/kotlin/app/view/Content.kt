package app.view

import app.store.*
import io.kvision.core.*
import io.kvision.html.*
import io.kvision.panel.*
import io.kvision.state.*


fun Container.Content() = div(classes = setOf("container")) {
  vPanel {
    h2("Libraries")
    responsiveGridPanel().bind(store) { (libraries) ->
      libraries.data.forEachIndexed { index, kotlinMPPLibrary ->
        options((index % 2) + 1, (index / 2) + 1) {
          LibraryCard(kotlinMPPLibrary)
        }
      }
    }
//    flexPanel(wrap = FlexWrap.WRAP, spacing = 15).bind(store) { (libraries) ->
//
//    }
  }
}
