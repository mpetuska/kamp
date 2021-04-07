package app.view

import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.staticStyle

@DslMarker
annotation class KampComponent

@KampComponent
fun RenderContext.App() {
  staticStyle(
    """
    /* width */
    ::-webkit-scrollbar {
      width: 0.75rem;
    }

    /* Track */
    ::-webkit-scrollbar-track {
      box-shadow: inset 0 0 5px grey;
      border-radius: 0.75rem;
    }
    
    /* Handle */
    ::-webkit-scrollbar-thumb {
      background: lightgray;
      box-shadow: inset 0 0 5px grey;
      border-radius: 0.75rem;
    }

    /* Handle on hover */
    ::-webkit-scrollbar-thumb:hover {
      background: gray;
    }
    """.trimIndent()
  )
  stackUp({
    height { "100%" }
    width { "100%" }
    position { relative {} }
  }) {
    Header()
    Content()
  }
}
