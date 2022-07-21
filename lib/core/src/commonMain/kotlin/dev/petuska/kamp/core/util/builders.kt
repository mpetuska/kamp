package dev.petuska.kamp.core.util

inline fun <T> buildIf(condition: Boolean, action: () -> T): T? = if (condition) action() else null
