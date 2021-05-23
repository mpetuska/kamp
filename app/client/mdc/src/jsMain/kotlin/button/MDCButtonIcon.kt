package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.Tag
import androidx.compose.web.css.StyleBuilder
import androidx.compose.web.elements.ElementScope
import androidx.compose.web.elements.I
import androidx.compose.web.elements.Text
import dev.petuska.kmdc.MDCDsl
import org.w3c.dom.HTMLElement

@MDCDsl
@Composable
inline fun ElementScope<*>.MDCButtonIcon(
  icon: String,
  crossinline attrs: AttrsBuilder<Tag>.() -> Unit = {},
  crossinline style: (StyleBuilder.() -> Unit) = {},
) {
  I(
    attrs = {
      classes("material-icons", "mdc-button__icon")
      prop<HTMLElement, String>(
        { e, v ->
          e.setAttribute("aria-hidden", v)
        },
        "true"
      )
      attrs()
    },
    style = style
  ) {
    Text(icon)
  }
}
