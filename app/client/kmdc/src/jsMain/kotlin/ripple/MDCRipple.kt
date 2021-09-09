package dev.petuska.kmdc.ripple

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.css.jsObject
import org.jetbrains.compose.web.dom.ElementScope
import org.w3c.dom.Element

@JsModule("@material/ripple")
private external object MDCRippleModule {
  interface MDCRippleAttachOpts {
    var isUnbounded: Boolean?
  }

  class MDCRipple(element: Element, opts: MDCRippleAttachOpts = definedExternally) {
    companion object {
      fun attachTo(element: Element, opts: MDCRippleAttachOpts = definedExternally)
    }
  }
}

public data class MDCRippleOpts(var isUnbounded: Boolean = false)

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-ripple)
 */
@MDCDsl
@Composable
public fun ElementScope<*>.MDCRipple(
  opts: Builder<MDCRippleOpts>? = null
) {
  val options = MDCRippleOpts().apply { opts?.invoke(this) }
  DomSideEffect {
    MDCRippleModule.MDCRipple.attachTo(
      element = it,
      opts = jsObject {
        isUnbounded = options.isUnbounded
      }
    )
  }
}
