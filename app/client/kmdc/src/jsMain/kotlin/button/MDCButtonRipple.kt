package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.ripple.MDCRipple
import dev.petuska.kmdc.ripple.MDCRippleOpts
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLSpanElement

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-button)
 */
@MDCDsl
@Composable
public fun MDCButtonScope.MDCButtonRipple(
  opts: Builder<MDCRippleOpts>? = null,
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
  MDCRipple(opts)
}
