package dev.petuska.kmdc.drawer

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.ComposableBuilder
import dev.petuska.kmdc.MDCAttrsDsl
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.mdc
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementScope
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@JsModule("@material/drawer/dist/mdc.drawer.css")
private external val MDCDrawerCSS: dynamic

@JsModule("@material/drawer")
private external object MDCDrawerModule {
  class MDCDrawer(element: Element) {
    companion object {
      fun attachTo(element: Element): MDCDrawer
    }

    var open: Boolean
  }
}

public data class MDCDrawerOpts(
  var type: Type = Type.Dismissible,
  var state: State = State.Closed,
  var isOpen: Boolean = false,
) {
  public enum class Type(public vararg val classes: String) {
    Dismissible("mdc-drawer--dismissible"),
    Modal("mdc-drawer--modal"),
  }

  public enum class State(public vararg val classes: String) {
    Closed,
    Open("mdc-drawer--open"),
    Opening("mdc-drawer--opening"),
    Closing("mdc-drawer--closing"),
  }
}

public class MDCDrawerScope(scope: ElementScope<HTMLElement>) : ElementScope<HTMLElement> by scope

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v13.0.0/packages/mdc-drawer)
 */
@MDCDsl
@Composable
public fun MDCDrawer(
  opts: Builder<MDCDrawerOpts>? = null,
  attrs: AttrBuilderContext<HTMLElement>? = null,
  content: ComposableBuilder<MDCDrawerScope>? = null
) {
  MDCDrawerCSS
  val options = MDCDrawerOpts().apply { opts?.invoke(this) }
  Aside(
    attrs = {
      classes("mdc-drawer", *options.type.classes, *options.state.classes)
      attrs?.invoke(this)
    },
  ) {
    DomSideEffect(null) {
      it.mdc = MDCDrawerModule.MDCDrawer.attachTo(element = it)
    }
    DomSideEffect(options.isOpen) {
      it.mdc<MDCDrawerModule.MDCDrawer> { open = options.isOpen }
    }
    content?.let { MDCDrawerScope(this).it() }
  }
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v13.0.0/packages/mdc-drawer)
 */
@MDCDsl
@Composable
public fun MDCDrawerScrim(
  attrs: AttrBuilderContext<HTMLDivElement>? = null,
  content: ContentBuilder<HTMLDivElement>? = null
) {
  Div(
    attrs = {
      classes("mdc-drawer-scrim")
      attrs?.invoke(this)
    },
    content = content,
  )
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v13.0.0/packages/mdc-drawer)
 */
@MDCAttrsDsl
public fun AttrsBuilder<*>.mdcDrawerAppContent() {
  classes("mdc-drawer-app-content")
}