package scanner.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
  override fun getValue(thisRef: R, property: KProperty<*>): Logger {
    val javaClass =
      thisRef.javaClass.let { java ->
        java.enclosingClass?.takeIf { it.kotlin.companionObject?.java == javaClass } ?: java
      }
    return getLogger(javaClass.name)
  }
}
