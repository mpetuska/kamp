package dev.petuska.kmdc.checkbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.Path
import dev.petuska.kmdc.Svg
import dev.petuska.kmdc.form.field.MDCFormFieldModule
import dev.petuska.kmdc.form.field.MDCFormFieldScope
import dev.petuska.kmdc.mdc
import dev.petuska.kmdc.ripple.MDCRippleModule
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.builders.InputAttrsBuilder
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.DomEffectScope
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import kotlin.random.Random

@JsModule("@material/checkbox/dist/mdc.checkbox.css")
public external val MDCCheckboxStyle: dynamic

@JsModule("@material/checkbox")
public external object MDCCheckboxModule {
  public class MDCCheckbox(element: Element) : MDCFormFieldModule.MDCFormFieldInput {
    public companion object {
      public fun attachTo(element: Element): MDCCheckbox
    }

    public var checked: Boolean
    public var indeterminate: Boolean
    public var disabled: Boolean
    public var value: String
    override val ripple: MDCRippleModule.MDCRipple?
  }
}

public data class MDCCheckboxOpts(
  public var disabled: Boolean = false,
  public var label: String? = null,
  public var indeterminate: Boolean = false,
)

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v13.0.0/packages/mdc-checkbox)
 */
@MDCDsl
@Composable
public fun MDCCheckbox(
  opts: Builder<MDCCheckboxOpts>? = null,
  attrs: (InputAttrsBuilder<Boolean>.() -> Unit)? = null,
) {
  MDCCheckboxBody(opts, attrs)
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v13.0.0/packages/mdc-checkbox)
 */
@MDCDsl
@Composable
public fun MDCFormFieldScope.MDCCheckbox(
  opts: Builder<MDCCheckboxOpts>? = null,
  attrs: (InputAttrsBuilder<Boolean>.() -> Unit)? = null,
) {
  MDCCheckboxBody(opts, attrs) { el, mdcCheckbox ->
    setInput(el, mdcCheckbox)
  }
}

@MDCDsl
@Composable
private fun MDCCheckboxBody(
  opts: Builder<MDCCheckboxOpts>? = null,
  attrs: (InputAttrsBuilder<Boolean>.() -> Unit)? = null,
  domSideEffect: (DomEffectScope.(HTMLDivElement, MDCCheckboxModule.MDCCheckbox) -> Unit)? = null,
) {
  MDCCheckboxStyle
  val options = MDCCheckboxOpts().apply { opts?.invoke(this) }
  val localId = remember { Random.nextInt(9999) }
  val checkboxId = remember { "mdc-checkbox__native-control__$localId" }

  Div(attrs = {
    classes("mdc-checkbox")
    if (options.disabled) classes("mdc-checkbox--disabled")
  }) {
    DomSideEffect {
      val mdc = MDCCheckboxModule.MDCCheckbox.attachTo(it)
      mdc.indeterminate = options.indeterminate
      it.mdc = mdc
      domSideEffect?.invoke(this, it, mdc)
    }
    DomSideEffect(options.indeterminate) {
      it.mdc<MDCCheckboxModule.MDCCheckbox> { indeterminate = options.indeterminate }
    }
    Input(type = InputType.Checkbox, attrs = {
      classes("mdc-checkbox__native-control")
      id(checkboxId)
      if (options.disabled) disabled()
      if (options.indeterminate) attr("data-indeterminate", "true")
      attrs?.invoke(this)
    })
    Div(attrs = {
      classes("mdc-checkbox__background")
    }) {
      Svg(attrs = {
        classes("mdc-checkbox__checkmark")
        attr("viewBox", "0 0 24 24")
      }) {
        Path(attrs = {
          classes("mdc-checkbox__checkmark-path")
          attr("fill", "none")
          attr("d", "M1.73,12.91 8.1,19.28 22.79,4.59")
        })
      }
      Div(attrs = { classes("mdc-checkbox__mixedmark") })
    }
    Div(attrs = { classes("mdc-checkbox__ripple") })
  }
  options.label?.let {
    Label(forId = checkboxId, attrs = { id("$checkboxId-label") }) { Text(it) }
  }
}