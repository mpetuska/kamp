package app.util

import kotlin.properties.*
import kotlin.reflect.*
import kotlin.reflect.full.*

class EnvDelegate<T>(private val converter: EnvDelegate<*>.(String?) -> T) : ReadOnlyProperty<Any, T> {
  private val camelSplitRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex()
  override fun getValue(thisRef: Any, property: KProperty<*>): T {
    val env = System.getenv()
    val value = env[property.name]
      ?: env[property.name.toUpperCase().replace("-", "_")]
      ?: env[property.name.split(camelSplitRegex).joinToString("_") { it.toUpperCase() }]
    return converter(value)
  }
  
  fun findEnv(name: String) = System.getenv()[name]
}

object Env {
  val API_URL by EnvDelegate {
    it ?: findEnv("WEBSITE_HOSTNAME")?.let { host -> "https://$host/api" } ?: "http://localhost:8080/api"
  }
  val MONGO_STRING by EnvDelegate { it ?: "mongodb://localhost:27017" }
  
  override fun toString(): String {
    return this::class.memberProperties.joinToString("\n") {
      @Suppress("UNCHECKED_CAST")
      "${it.name.toUpperCase()}=${(it as KProperty1<Any, *>).get(this)}"
    }
  }
}
