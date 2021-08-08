package dev.petuska.kmdc.card

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-card)
 */
@MDCDsl
@Composable
fun MDCCardScope.MDCCardPrimaryAction(
  attrs: AttrBuilderContext<HTMLDivElement>? = null,
  content: ContentBuilder<HTMLDivElement>? = null
) {
  Div(
    attrs = {
      classes("mdc-card__primary-action")
      tabIndex(0)
      attrs?.invoke(this)
    },
  ) {
    content?.invoke(this)
    Div(
      attrs = {
        classes("mdc-card__ripple")
      }
    )
  }
}
