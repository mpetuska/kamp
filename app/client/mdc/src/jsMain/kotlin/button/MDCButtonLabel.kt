package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLSpanElement

@MDCDsl
@Composable
inline fun MDCButtonScope.MDCButtonLabel(
  crossinline attrs: AttrsBuilder<HTMLSpanElement>.() -> Unit = {},
  noinline content: ContentBuilder<HTMLSpanElement> = {}
) {
  Span(
    attrs = {
      classes("mdc-button__label")
      attrs()
    },
    content = content
  )
}

@MDCDsl
@Composable
inline fun MDCButtonScope.MDCButtonLabel(
  content: String,
  crossinline attrs: AttrsBuilder<HTMLSpanElement>.() -> Unit = {},
) {
  MDCButtonLabel(attrs) {
    Text(content)
  }
}
