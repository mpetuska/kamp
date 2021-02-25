package kamp.util

import kotlin.properties.*
import kotlin.reflect.*
import kotlin.reflect.full.*

public class EnvDelegate<T>(private val converter: EnvDelegate<*>.(String?) -> T) : ReadOnlyProperty<Any, T> {
  private val camelSplitRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()
  override fun getValue(thisRef: Any, property: KProperty<*>): T {
    val env = System.getenv()
    val value = env[property.name]
      ?: env[property.name.toUpperCase().replace("-", "_")]
      ?: env[property.name.split(camelSplitRegex).joinToString("_") { it.toUpperCase() }]
    return converter(value)
  }

  public fun findEnv(name: String): String? = System.getenv()[name]
}

public abstract class Env {
  override fun toString(): String = this::class.memberProperties.joinToString("\n") {
    @Suppress("UNCHECKED_CAST")
    "${it.name.toUpperCase()}=${(it as KProperty1<Any, *>).get(this)}"
  }
}
