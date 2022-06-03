package dev.petuska.kamp.core.util

import org.slf4j.LoggerFactory

inline fun <reified T : Any> T.logger() = LoggerFactory.getLogger(T::class.java)
