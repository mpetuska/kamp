package dev.petuska.kmdc.list

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.ComposableBuilder
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLHeadingElement

public class MDCListGroupScope(scope: ElementScope<HTMLDivElement>) : ElementScope<HTMLDivElement> by scope

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v13.0.0/packages/mdc-deprecated-list)
 */
@MDCDsl
@Composable
@Deprecated("Based on already deprecated MDC List. New implementation is still in the works.", level = DeprecationLevel.WARNING)
public fun MDCListGroup(
  attrs: Builder<AttrsBuilder<HTMLDivElement>>? = null,
  content: ComposableBuilder<MDCListGroupScope>? = null,
) {
  Div(attrs = {
    classes("mdc-deprecated-list-group")
    attrs?.invoke(this)
  }, content = content?.let { { MDCListGroupScope(this).it() } })
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v13.0.0/packages/mdc-deprecated-list)
 */
@MDCDsl
@Composable
public fun MDCListGroupScope.MDCListGroupSubheader(
  attrs: Builder<AttrsBuilder<HTMLHeadingElement>>? = null,
  content: ContentBuilder<HTMLHeadingElement>? = null,
) {
  H3(attrs = {
    classes("mdc-deprecated-list-group__subheader")
    attrs?.invoke(this)
  }, content = content)
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v13.0.0/packages/mdc-deprecated-list)
 */
@MDCDsl
@Composable
public fun MDCListGroupScope.MDCListGroupSubheader(
  text: String,
  attrs: Builder<AttrsBuilder<HTMLHeadingElement>>? = null,
) {
  MDCListGroupSubheader(attrs) { Text(text) }
}