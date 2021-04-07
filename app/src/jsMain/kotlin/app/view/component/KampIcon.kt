package app.view.component

import app.util.styled
import app.view.KampComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.BoxParams

@KampComponent
fun RenderContext.KampIcon(style: BoxParams.() -> Unit = {}) {
  styled(::img)(style) {
    src("/kamp.svg")
  }
}
