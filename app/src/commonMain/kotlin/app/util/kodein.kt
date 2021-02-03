package app.util

import org.kodein.di.*
import kotlin.properties.*
import kotlin.reflect.*

class DIModule(
  private val allowSilentOverride: Boolean = false,
  private val prefix: String = "",
  private val init: DI.Builder.() -> Unit,
) : ReadOnlyProperty<Any?, DI.Module> {
  private var module: DI.Module? = null
  override fun getValue(thisRef: Any?, property: KProperty<*>): DI.Module {
    return module ?: DI.Module(property.name, allowSilentOverride, prefix, init).also { module = it }
  }
}
