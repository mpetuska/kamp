package app.util

import kotlin.properties.*
import kotlin.reflect.*

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
  val port by EnvDelegate { it?.toIntOrNull() ?: 8080 }
}
