package app.component

import app.config.*
import app.service.*
import app.util.*
import org.kodein.di.*
import react.dom.*


val App by FC {
  nav("navbar navbar-expand-lg navbar-dark bg-dark") {
    a(classes = "navbar-brand") {
      img(
        classes = "d-inline-block align-top",
        src = "/kamp.svg"
      ) {
        attrs {
          width = "30"
          height = "30"
        }
      }
      +"KAMP"
    }
  }
//  Navbar {}
  div(classes = "container") {
    div("row") {
      h2 {
        +"Libraries"
      }
    }
    val service by di.instance<LibraryService>()
    PaginatedContainer({ service.getAll(it, 50) }) { libraries ->
      div("row card-columns") {
        for (library in libraries) {
          LibraryCard {
            attrs { lib = library }
          }
        }
      }
    }
  }
}
