package dev.petuska.kmdc.icon.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.I
import org.w3c.dom.HTMLElement

@Composable
private fun render(
  opts: Builder<MDCIconButtonOpts>?,
  attrs: AttrBuilderContext<HTMLElement>?,
  content: ContentBuilder<HTMLElement>?,
) {
  val options = MDCIconButtonOpts().apply { opts?.invoke(this) }
  I(
    attrs = {
      classes(
        *listOfNotNull(
          "mdc-icon-button__icon",
          if (options.on) "mdc-icon-button__icon--on" else null
        ).toTypedArray()
      )
      attrs?.invoke(this)
    },
    content = content,
  )
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-icon-button)
 */
@MDCDsl
@Composable
fun MDCIconButtonScope.MDCIconButtonIcon(
  opts: Builder<MDCIconButtonOpts>? = null,
  attrs: AttrBuilderContext<HTMLElement>? = null,
  content: ContentBuilder<HTMLElement>? = null,
) = render(opts, attrs, content)

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-icon-button)
 */
@MDCDsl
@Composable
fun MDCIconLinkScope.MDCIconButtonIcon(
  opts: Builder<MDCIconButtonOpts>? = null,
  attrs: AttrBuilderContext<HTMLElement>? = null,
  content: ContentBuilder<HTMLElement>? = null,
) = render(opts, attrs, content)
