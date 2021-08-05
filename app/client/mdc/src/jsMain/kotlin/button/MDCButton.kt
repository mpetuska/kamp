package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.ComposableBuilder
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.ripple.MDCRipple
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLButtonElement

@JsModule("@material/button/dist/mdc.button.css")
private external val MDCButtonStyle: dynamic

data class MDCButtonOpts(
  var type: Type = Type.Text,
  var icon: MDCButtonIconType = MDCButtonIconType.None
) {
  enum class Type(vararg val classes: String) {
    Text, Outline("mdc-button--outline"), Contained("mdc-button--raised"), ContainedUnelevated("mdc-button--unelevated")
  }

  enum class MDCButtonIconType(vararg val classes: String) {
    None, Leading("mdc-button--icon-leading"), Trailing("mdc-button--icon-trailing")
  }
}

class MDCButtonScope(scope: ElementScope<HTMLButtonElement>) : ElementScope<HTMLButtonElement> by scope

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-button)
 */
@MDCDsl
@Composable
fun MDCButton(
  opts: Builder<MDCButtonOpts>? = null,
  attrs: AttrBuilderContext<HTMLButtonElement>? = null,
  content: ComposableBuilder<MDCButtonScope>? = null
) {
  MDCButtonStyle
  val options = MDCButtonOpts().apply { opts?.invoke(this) }
  Button(
    attrs = {
      classes("mdc-button", *options.type.classes, *options.icon.classes)
      attrs?.invoke(this)
    }
  ) {
    MDCRipple()
    content?.let { MDCButtonScope(this).it() }
  }
}

@MDCDsl
@Composable
fun MDCButton(
  text: String,
  opts: Builder<MDCButtonOpts>? = null,
  attrs: AttrBuilderContext<HTMLButtonElement>? = null,
) {
  MDCButton(opts, attrs) {
    Text(text)
  }
}
