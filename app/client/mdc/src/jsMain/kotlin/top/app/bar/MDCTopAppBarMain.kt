package dev.petuska.kmdc.top.app.bar

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Main
import org.w3c.dom.HTMLElement

/**
 * If using an [MDCTopAppBar] component, all the page content must be placed into this [MDCTopAppBarMain] container.
 */
@MDCDsl
@Composable
fun MDCTopAppBarMain(
  type: MDCTopAppBarOpts.Type,
  attrs: AttrBuilderContext<HTMLElement>? = null,
  content: ContentBuilder<HTMLElement>? = null
) {
  Main(
    attrs = {
      classes(type.mainAdjustClass)
      attrs?.invoke(this)
    },
    content = content
  )
}
