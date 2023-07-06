package dev.petuska.kodex.cli.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
  override fun getValue(thisRef: R, property: KProperty<*>): Logger {
    val javaClass = thisRef.javaClass.let { java ->
      java.enclosingClass ?: java
    }
    return LoggerFactory.getLogger(javaClass)
  }
}
