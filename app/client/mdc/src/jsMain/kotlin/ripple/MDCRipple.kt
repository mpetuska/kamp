package dev.petuska.kmdc.ripple

import androidx.compose.runtime.Composable
import androidx.compose.web.elements.ElementScope
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.jsObject
import org.w3c.dom.Element

@JsModule("@material/ripple")
internal external object MDCRippleModule {
  interface MDCRippleAttachOpts {
    var isUnbounded: Boolean?
  }

  class MDCRipple(element: Element, opts: MDCRippleAttachOpts = definedExternally) {
    companion object {
      fun attachTo(element: Element, opts: MDCRippleAttachOpts = definedExternally)
    }
  }
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v11.0.0/packages/mdc-ripple)
 */
@MDCDsl
@Composable
fun ElementScope<*>.MDCRipple(isUnbounded: Boolean = false) = DomSideEffect {
  MDCRippleModule.MDCRipple.attachTo(
    it,
    jsObject {
      this.isUnbounded = isUnbounded
    }
  )
}
