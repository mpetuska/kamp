package dev.petuska.kmdc

import androidx.compose.runtime.Composable
import org.w3c.dom.Element

@JsName("require")
public external fun requireJsModule(module: String): dynamic

@DslMarker
public annotation class MDCDsl

@DslMarker
public annotation class MDCAttrsDsl

public typealias Builder<T> = T.() -> Unit
public typealias ComposableBuilder<T> = @Composable Builder<T>

public data class Wrapper<T>(val value: T)

public fun <T> T.wrap(): Wrapper<T> = Wrapper(this)

internal var Element.mdc: dynamic
  get() = asDynamic().mdc
  set(value) {
    asDynamic().mdc = value
  }

internal fun <T> Element.mdc(): T = mdc.unsafeCast<T>()
