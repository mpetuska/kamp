package app.view.component

import app.util.*
import app.view.*
import dev.fritz2.dom.html.*
import dev.fritz2.styling.params.*

@KampComponent
fun RenderContext.KampIcon(style: BoxParams.() -> Unit = {}) {
  styled(::img)(style) {
    src("/kamp.svg")
  }
}
