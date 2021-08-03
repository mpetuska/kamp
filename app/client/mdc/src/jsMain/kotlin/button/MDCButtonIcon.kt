package dev.petuska.kmdc.button

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

@MDCDsl
@Composable
inline fun ElementScope<*>.MDCButtonIcon(
  icon: String,
  crossinline attrs: AttrsBuilder<HTMLElement>.() -> Unit = {},
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
  ) {
    Text(icon)
  }
}
