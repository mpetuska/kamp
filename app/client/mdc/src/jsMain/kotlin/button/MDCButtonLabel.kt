package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.Tag
import androidx.compose.web.css.StyleBuilder
import androidx.compose.web.elements.ElementScope
import androidx.compose.web.elements.Span
import androidx.compose.web.elements.Text
import dev.petuska.kmdc.MDCDsl
import org.w3c.dom.HTMLSpanElement

@MDCDsl
@Composable
inline fun MDCButtonLabel(
  crossinline attrs: AttrsBuilder<Tag.Span>.() -> Unit = {},
  crossinline style: (StyleBuilder.() -> Unit) = {},
  content: @Composable ElementScope<HTMLSpanElement>.() -> Unit = {}
) {
  Span(
    attrs = {
      classes("mdc-button__label")
      attrs()
    },
    style = style, content
  )
}

@MDCDsl
@Composable
inline fun MDCButtonLabel(
  content: String,
  crossinline attrs: AttrsBuilder<Tag.Span>.() -> Unit = {},
  crossinline style: (StyleBuilder.() -> Unit) = {},
) {
  MDCButtonLabel(
    attrs, style
  ) {
    Text(content)
  }
}
