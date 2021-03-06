package app.view

import dev.fritz2.components.*
import dev.fritz2.dom.html.*

@DslMarker
annotation class KampComponent

@KampComponent
fun RenderContext.App() {
  stackUp({
    height { "100%" }
    width { "100%" }
    position { relative {} }
  }) {
    Header()
    Content()
  }
}
