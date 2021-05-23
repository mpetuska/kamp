package dev.petuska.kmdc.icon.button

import androidx.compose.runtime.Composable
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.Tag
import androidx.compose.web.css.StyleBuilder
import androidx.compose.web.elements.Button
import androidx.compose.web.elements.ElementScope
import androidx.compose.web.elements.Text
import dev.petuska.kmdc.MDCDsl
import org.w3c.dom.Element
import org.w3c.dom.HTMLHeadingElement

@PublishedApi
@JsModule("@material/icon-button/dist/mdc.icon-button.css")
internal external val MDCIconButtonStyle: dynamic

@PublishedApi
@JsModule("@material/icon-button")
internal external object MDCIconButtonModule {
  class MDCIconButtonToggle(element: Element) {
    companion object {
      fun attachTo(element: Element)
    }
  }
}

data class MDCIconButtonOpts(
  var on: Boolean = false,
)

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v11.0.0/packages/mdc-icon-button)
 */
@MDCDsl
@Composable
inline fun MDCIconButton(
  opts: MDCIconButtonOpts.() -> Unit = {},
  crossinline attrs: AttrsBuilder<Tag.Button>.() -> Unit = {},
  crossinline style: (StyleBuilder.() -> Unit) = {},
  content: @Composable ElementScope<HTMLHeadingElement>.() -> Unit
) {
  MDCIconButtonStyle
  val options = MDCIconButtonOpts().apply(opts)
  Button(
    attrs = {
      classes(*listOfNotNull("mdc-icon-button", if (options.on) "mdc-icon-button--on" else null).toTypedArray())
      attrs()
    },
    style,
  ) {
    DomSideEffect {
      MDCIconButtonModule.MDCIconButtonToggle.attachTo(it)
    }
    content()
  }
}

@MDCDsl
@Composable
inline fun MDCIconButton(
  icon: String,
  crossinline attrs: AttrsBuilder<Tag.Button>.() -> Unit = {},
  crossinline style: (StyleBuilder.() -> Unit) = {},
) {
  MDCIconButton(
    attrs = {
      classes("mdc-icon-button", "material-icons")
      attrs()
    },
    style = style,
  ) {
    Text(icon)
  }
}
