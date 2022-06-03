package dev.petuska.kamp.core.util

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class Env {
  protected class EnvDelegate<T>(private val converter: EnvDelegate<*>.(String?) -> T) : ReadOnlyProperty<Any, T> {
    private val camelSplitRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
      val env = System.getenv()
      val value = env[property.name]
        ?: env[property.name.uppercase().replace("-", "_")]
        ?: env[property.name.split(camelSplitRegex).joinToString("_") { it.uppercase() }]
      return converter(value)
    }

    fun findEnv(name: String): String? = System.getenv()[name]
  }
}
