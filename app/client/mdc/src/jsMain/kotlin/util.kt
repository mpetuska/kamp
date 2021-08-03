package dev.petuska.kmdc

import androidx.compose.runtime.Composable

internal external fun require(module: String): dynamic

@DslMarker
annotation class MDCDsl

typealias ComposableBuilder<T> = @Composable T.() -> Unit
