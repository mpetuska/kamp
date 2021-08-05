package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.ripple.MDCRipple
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLSpanElement

@MDCDsl
@Composable
fun MDCButtonScope.MDCButtonRipple(
  isUnbounded: Boolean = false,
  attrs: AttrBuilderContext<HTMLSpanElement>? = null,
  content: ContentBuilder<HTMLSpanElement>? = null,
) {
  Span(
    attrs = {
      classes("mdc-button__ripple")
      attrs?.invoke(this)
    },
    content = content
  )
  MDCRipple(isUnbounded)
}
