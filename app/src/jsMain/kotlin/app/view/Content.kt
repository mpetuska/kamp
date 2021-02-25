package app.view

import app.store.*
import io.kvision.core.*
import io.kvision.html.*
import io.kvision.panel.*
import io.kvision.state.*


fun Container.Content() = vPanel {
  h2("Libraries")
  flexPanel(wrap = FlexWrap.WRAP, spacing = 15).bind(store) { (libraries) ->
    for (library in libraries.data) {
      LibraryCard(library)
    }
  }
}
