package dev.petuska.kmdc.top.app.bar

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.ComposableBuilder
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.Section
import org.w3c.dom.HTMLElement

class MDCTopAppBarSectionScope(scope: ElementScope<HTMLElement>) : ElementScope<HTMLElement> by scope

data class MDCTopAppBarSectionOpts(
  var align: Align = Align.Start,
  var role: String? = null,
) {
  enum class Align(vararg val classes: String) {
    Start("mdc-top-app-bar__section--align-start"),
    End("mdc-top-app-bar__section--align-end"),
  }
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-top-app-bar)
 */
@MDCDsl
@Composable
fun MDCTopAppBarRowScope.MDCTopAppBarSection(
  opts: Builder<MDCTopAppBarSectionOpts>? = null,
  attrs: AttrBuilderContext<HTMLElement>? = null,
  content: ComposableBuilder<MDCTopAppBarSectionScope>? = null
) {
  val options = MDCTopAppBarSectionOpts().apply { opts?.invoke(this) }
  Section(
    attrs = {
      classes("mdc-top-app-bar__section", *options.align.classes)
      options.role?.let {
        attr("role", it)
      }
      attrs?.invoke(this)
    },
    content = content?.let { { MDCTopAppBarSectionScope(this).it() } }
  )
}
