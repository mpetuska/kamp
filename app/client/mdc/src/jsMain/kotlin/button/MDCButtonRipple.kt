package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.Tag
import androidx.compose.web.css.StyleBuilder
import androidx.compose.web.elements.ElementScope
import androidx.compose.web.elements.Span
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.ripple.MDCRipple
import org.w3c.dom.HTMLSpanElement

@MDCDsl
@Composable
inline fun ElementScope<*>.MDCButtonRipple(
  isUnbounded: Boolean = false,
  crossinline attrs: AttrsBuilder<Tag.Span>.() -> Unit = {},
  crossinline style: (StyleBuilder.() -> Unit) = {},
  content: @Composable ElementScope<HTMLSpanElement>.() -> Unit = {}
) {
  Span(
    attrs = {
      classes("mdc-button__ripple")
      attrs()
    },
    style = style, content
  )
  MDCRipple(isUnbounded)
}
