package app.view.component

import app.util.*
import app.view.*
import dev.fritz2.dom.html.*

@KampComponent
fun RenderContext.Link(href: String, target: String? = null, block: A.() -> Unit = {}) {
  styled(::div)({
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
  }) {
    styled(::a)({
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
