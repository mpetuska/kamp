package dev.petuska.kmdc.icon.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement

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
  crossinline attrs: AttrsBuilder<HTMLButtonElement>.() -> Unit = {},
  crossinline content: ContentBuilder<HTMLButtonElement> = {}
) {
  MDCIconButtonStyle
  val options = MDCIconButtonOpts().apply(opts)
  Button(
    attrs = {
      classes(*listOfNotNull("mdc-icon-button", if (options.on) "mdc-icon-button--on" else null).toTypedArray())
      attrs()
    },
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
  crossinline attrs: AttrsBuilder<HTMLButtonElement>.() -> Unit = {},
) {
  MDCIconButton(
    attrs = {
      classes("mdc-icon-button", "material-icons")
      attrs()
    },
  ) {
    Text(icon)
  }
}
