package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ContentBuilder
import org.w3c.dom.HTMLButtonElement

@PublishedApi
@JsModule("@material/button/dist/mdc.button.css")
internal external val MDCButtonStyle: dynamic

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

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v11.0.0/packages/mdc-button)
 */
@MDCDsl
@Composable
inline fun MDCButton(
  opts: MDCButtonOpts.() -> Unit = {},
  crossinline attrs: AttrsBuilder<HTMLButtonElement>.() -> Unit = {},
  noinline content: ContentBuilder<HTMLButtonElement> = {}
) {
  MDCButtonStyle
  val options = MDCButtonOpts().apply(opts)
  Button(
    attrs = {
      classes("mdc-button", *options.type.classes, *options.icon.classes)
      attrs()
    },
    content
  )
}
