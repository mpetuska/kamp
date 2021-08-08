package dev.petuska.kmdc

import androidx.compose.runtime.Composable

@JsName("require")
external fun requireJsModule(module: String): dynamic

@DslMarker
annotation class MDCDsl

typealias Builder<T> = T.() -> Unit
typealias ComposableBuilder<T> = @Composable Builder<T>
