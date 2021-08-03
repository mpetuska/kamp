package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.ripple.MDCRipple
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLSpanElement

@MDCDsl
@Composable
inline fun MDCButtonScope.MDCButtonRipple(
  isUnbounded: Boolean = false,
  crossinline attrs: AttrsBuilder<HTMLSpanElement>.() -> Unit = {},
  noinline content: ContentBuilder<HTMLSpanElement> = {}
) {
  Span(
    attrs = {
      classes("mdc-button__ripple")
      attrs()
    },
    content = content
  )
  MDCRipple(isUnbounded)
}
