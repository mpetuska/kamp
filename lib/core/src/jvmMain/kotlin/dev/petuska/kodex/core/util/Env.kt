package dev.petuska.kodex.core.util

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class Env {
  private val variables = mutableMapOf<String, Any?>()

  protected inner class EnvDelegate<T>(
    private val converter: EnvDelegate<*>.(String?) -> T
  ) : ReadOnlyProperty<Any, T> {
    private val env = System.getenv()
    private val camelSplitRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()

    private var value: T? = null
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
      return value ?: converter(
        env[property.name]
          ?: env[property.name.uppercase().replace("-", "_")]
          ?: env[property.name.split(camelSplitRegex).joinToString("_") { it.uppercase() }]
      ).also {
        variables[property.name] = it
        value = it
      }
    }

    fun findEnv(name: String): String? = env[name]
  }

  override fun toString(): String {
    return variables.entries.joinToString("\n") { (key, value) -> "$key=$value" }
  }
}
