package dev.petuska.kmdc.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.MDCDsl
import dev.petuska.kmdc.mdc
import dev.petuska.kmdc.ripple.MDCRippleModule
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.builders.InputAttrsBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import kotlin.random.Random

@JsModule("@material/textfield/dist/mdc.textfield.css")
private external val MDCTextFieldStyle: dynamic

@JsModule("@material/textfield")
private external object MDCTextFieldModule {
  class MDCTextField(element: Element) {
    companion object {
      fun attachTo(element: Element)
    }

    var value: String
    var disabled: Boolean
    var valid: Boolean
    var prefixText: String
    var suffixText: String

    // Proxied from input element
    var required: Boolean
    var pattern: String
    var minLength: Number
    var maxLength: Number
    var min: Number
    var max: Number
    var step: Number

    // Write-only
    var useNativeValidation: Boolean
    var helperTextContent: String
    var ripple: MDCRippleModule.MDCRipple
    var leadingIconAriaLabel: String
    var trailingIconAriaLabel: String
    var leadingIconContent: String
    var trailingIconContent: String

    fun focus()
    fun layout()
  }
}

public data class MDCTextFieldOpts(
  var type: Type = Type.Filled,
  var disabled: Boolean = false,
  var label: String? = null,
  var helperText: String? = null,
) {
  public enum class Type(public vararg val classes: String) {
    Filled("mdc-text-field--filled"), Outlined("mdc-text-field--outlined")
  }
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-textfield)
 */
@MDCDsl
@Composable
public fun MDCTextField(
  opts: Builder<MDCTextFieldOpts>? = null,
  attrs: (InputAttrsBuilder<String>.() -> Unit)? = null,
) {
  MDCTextFieldStyle
  val options = MDCTextFieldOpts().apply { opts?.invoke(this) }
  val labelId = remember { "mdc-floating-label__${Random.nextInt(9999)}" }
  val helperId = remember { "mdc-text-field-helper-text__${Random.nextInt(9999)}" }
  Label(
    attrs = {
      classes("mdc-text-field", *options.type.classes)
      if (options.label == null) classes("mdc-text-field--no-label")
      if (options.disabled) classes("mdc-text-field--disabled")
    }
  ) {
    DomSideEffect {
      it.mdc = MDCTextFieldModule.MDCTextField.attachTo(it)
    }
    when (options.type) {
      MDCTextFieldOpts.Type.Filled -> {
        Span(attrs = { classes("mdc-text-field__ripple") })
        options.label?.let {
          Span(attrs = {
            classes("mdc-floating-label")
            id("mdc-floating-label__$labelId")
          }) { Text(it) }
        }
        MDCTextFieldInput(options, attrs, labelId, helperId)
        Span(attrs = { classes("mdc-line-ripple") })
      }
      MDCTextFieldOpts.Type.Outlined -> {
        Span(attrs = { classes("mdc-notched-outline") }) {
          Span(attrs = { classes("mdc-notched-outline__leading") })
          Span(attrs = { classes("mdc-notched-outline__notch") }) {
            options.label?.let {
              Span(attrs = {
                classes("mdc-floating-label")
                id("mdc-floating-label__$labelId")
              }) { Text(it) }
            }
          }
          Span(attrs = { classes("mdc-notched-outline__trailing") })
        }
        MDCTextFieldInput(options, attrs, labelId, helperId)
      }
    }
  }
  options.helperText?.let {
    Div(attrs = { classes("mdc-text-field-helper-line") }) {
      Div(attrs = {
        classes("mdc-text-field-helper-text")
        id(helperId)
        attr("aria-hidden", "true")
      }) {
        Text(it)
      }
    }
  }
}

@Composable
private fun MDCTextFieldInput(
  options: MDCTextFieldOpts,
  attrs: (InputAttrsBuilder<String>.() -> Unit)?,
  labelId: String,
  helperId: String,
) {
  Input(InputType.Text, attrs = {
    classes("mdc-text-field__input")
    attr("aria-labelledby", labelId)
    options.helperText?.let {
      attr("aria-controls", helperId)
      attr("aria-describedby", helperId)
    }
    attrs?.invoke(this)
  })
}
