package dev.petuska.kmdc

internal external fun require(module: String): dynamic

internal fun <T> jsObject(builder: T.() -> Unit): T = js("{}").unsafeCast<T>().apply(builder)

@DslMarker
annotation class MDCDsl
