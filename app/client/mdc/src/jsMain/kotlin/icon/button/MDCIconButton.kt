package dev.petuska.kmdc.icon.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.ComposableBuilder
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.ripple.MDCRipple
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementScope
import org.w3c.dom.Element
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLButtonElement

@JsModule("@material/icon-button/dist/mdc.icon-button.css")
private external val MDCIconButtonStyle: dynamic

@JsModule("@material/icon-button")
private external object MDCIconButtonModule {
  class MDCIconButtonToggle(element: Element) {
    companion object {
      fun attachTo(element: Element)
    }
  }
}

data class MDCIconButtonOpts(
  var on: Boolean = false,
)

class MDCIconButtonScope(scope: ElementScope<HTMLButtonElement>) : ElementScope<HTMLButtonElement> by scope

class MDCIconLinkScope(scope: ElementScope<HTMLAnchorElement>) : ElementScope<HTMLAnchorElement> by scope

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-icon-button)
 */
@MDCDsl
@Composable
fun MDCIconButton(
  opts: Builder<MDCIconButtonOpts>? = null,
  attrs: AttrBuilderContext<HTMLButtonElement>? = null,
  content: ComposableBuilder<MDCIconButtonScope>? = null
) {
  MDCIconButtonStyle
  val options = MDCIconButtonOpts().apply { opts?.invoke(this) }
  Button(
    attrs = {
      classes(*listOfNotNull("mdc-icon-button", if (options.on) "mdc-icon-button--on" else null).toTypedArray())
      attrs?.invoke(this)
    },
  ) {
    DomSideEffect {
      MDCIconButtonModule.MDCIconButtonToggle.attachTo(it)
    }
    MDCRipple()
    Div(
      attrs = {
        classes("mdc-icon-button__ripple")
      }
    )
    content?.let { MDCIconButtonScope(this).it() }
  }
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-icon-button)
 */
@MDCDsl
@Composable
fun MDCIconLink(
  opts: Builder<MDCIconButtonOpts>? = null,
  attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
  content: ComposableBuilder<MDCIconLinkScope>? = null
) {
  MDCIconButtonStyle
  val options = MDCIconButtonOpts().apply { opts?.invoke(this) }
  A(
    attrs = {
      classes(*listOfNotNull("mdc-icon-button", if (options.on) "mdc-icon-button--on" else null).toTypedArray())
      attrs?.invoke(this)
    },
  ) {
    DomSideEffect {
      MDCIconButtonModule.MDCIconButtonToggle.attachTo(it)
    }
    MDCRipple()
    Div(
      attrs = {
        classes("mdc-icon-button__ripple")
      }
    )
    content?.let { MDCIconLinkScope(this).it() }
  }
}
