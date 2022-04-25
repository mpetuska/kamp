package kamp.util

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

public abstract class Env {
  protected class EnvDelegate<T>(private val converter: EnvDelegate<*>.(String?) -> T) : ReadOnlyProperty<Any, T> {
    private val camelSplitRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
      val env = System.getenv()
      val value = env[property.name]
        ?: env[property.name.uppercase().replace("-", "_")]
        ?: env[property.name.split(camelSplitRegex).joinToString("_") { it.uppercase() }]
      return converter(value)
    }

    public fun findEnv(name: String): String? = System.getenv()[name]
  }

  override fun toString(): String = this::class.memberProperties.joinToString("\n") {
    @Suppress("UNCHECKED_CAST")
    "${it.name.uppercase()}=${(it as KProperty1<Any, *>).get(this)}"
  }
}
