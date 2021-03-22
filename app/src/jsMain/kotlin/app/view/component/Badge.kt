package app.view.component

import app.util.*
import app.view.*
import dev.fritz2.dom.html.*
import dev.fritz2.styling.params.*
import dev.fritz2.styling.theme.*

@KampComponent
fun RenderContext.Badge(
  color: (Colors.() -> Property) = { primary.base },
  style: BoxParams.() -> Unit = {},
  content: Span.() -> Unit = {},
) = styled(::span)({
  css("border-radius: 0.75rem")
  css("background: none repeat scroll 0% 0%")
  boxShadow { flat }
  display { inlineFlex }
  fontWeight { "500" }
  minHeight { large }
  minWidth { "1.5rem" }
  alignItems { center }
  justifyContent { spaceAround }
  textAlign { center }
  paddings {
    horizontal { small }
    vertical { tiny }
  }
  textShadow { flat }
  fontSize(
    sm = { smaller },
    md = { small }
  )
  background {
    color(color)
  }
  color { neutral }
  style()
}, content)

