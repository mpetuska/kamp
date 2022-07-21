package dev.petuska.kamp.core.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Count(val count: Long)
