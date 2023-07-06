package dev.petuska.kodex.core.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Count(val count: Long)
