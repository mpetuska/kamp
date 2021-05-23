package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.Tag
import androidx.compose.web.css.StyleBuilder
import androidx.compose.web.elements.Button
import androidx.compose.web.elements.ElementScope
import dev.petuska.kmdc.MDCDsl
import org.w3c.dom.HTMLHeadingElement

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
  crossinline attrs: AttrsBuilder<Tag.Button>.() -> Unit = {},
  crossinline style: (StyleBuilder.() -> Unit) = {},
  content: @Composable ElementScope<HTMLHeadingElement>.() -> Unit
) {
  MDCButtonStyle
  val options = MDCButtonOpts().apply(opts)
  Button(
    attrs = {
      classes("mdc-button", *options.type.classes, *options.icon.classes)
      attrs()
    },
    style,
    content
  )
}
