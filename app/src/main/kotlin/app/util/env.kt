package app.util

import kotlin.properties.*
import kotlin.reflect.*
import kotlin.reflect.full.*

class EnvDelegate<T>(private val converter: (String?) -> T) : ReadOnlyProperty<Any, T> {
  private val camelSplitRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()
  override fun getValue(thisRef: Any, property: KProperty<*>): T {
    val env = System.getenv()
    val value = env[property.name]
      ?: env[property.name.toUpperCase().replace("-", "_")]
      ?: env[property.name.split(camelSplitRegex).joinToString("_") { it.toUpperCase() }]
    return converter(value)
  }
}

object Env {
  val PORT by EnvDelegate { it?.toIntOrNull() ?: 8080 }
  
  override fun toString(): String {
    return this::class.memberProperties.joinToString("\n") {
      @Suppress("UNCHECKED_CAST")
      "${it.name.toUpperCase()}=${(it as KProperty1<Any, *>).get(this)}"
    }
  }
}
