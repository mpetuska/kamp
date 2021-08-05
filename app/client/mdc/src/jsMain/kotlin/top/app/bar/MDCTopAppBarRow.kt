package dev.petuska.kmdc.top.app.bar

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.ComposableBuilder
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementScope
import org.w3c.dom.HTMLDivElement

class MDCTopAppBarRowScope(scope: ElementScope<HTMLDivElement>) : ElementScope<HTMLDivElement> by scope

@MDCDsl
@Composable
fun MDCTopAppBarScope.MDCTopAppBarRow(
  attrs: AttrBuilderContext<HTMLDivElement>? = null,
  content: ComposableBuilder<MDCTopAppBarRowScope>? = null
) {
  Div(
    attrs = {
      classes("mdc-top-app-bar__row")
      attrs?.invoke(this)
    },
    content = content?.let { { MDCTopAppBarRowScope(this).it() } }
  )
}
