package app.view.component

import app.view.*
import dev.fritz2.dom.html.*
import dev.fritz2.styling.params.*

@KampComponent
fun RenderContext.NavAnchor(href: String, target: String? = null, block: A.() -> Unit = {}) {
  (::div.styled {
    radius { small }
    border {
      width { none }
    }
    hover {
      background {
        color { lighterGray }
      }
    }
    paddings {
      top { tiny }
      bottom { tiny }
      left { small }
      right { small }
    }
  }){
    (::a.styled {
      fontSize { normal }
      fontWeight { semiBold }
      color { dark }
    }) {
      href(href)
      target?.let { target(it) }
      block()
    }
  }
}
