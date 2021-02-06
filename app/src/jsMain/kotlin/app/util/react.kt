package app.util

import react.*
import kotlin.properties.*
import kotlin.reflect.*

class FC<P : RProps>(private val render: RBuilder.(P) -> Unit) : ReadOnlyProperty<Any?, RClass<P>> {
  private var ref: RClass<P>? = null
  override fun getValue(thisRef: Any?, property: KProperty<*>): RClass<P> {
    return ref ?: rFunction(property.name, render).also { ref = it }
  }
}

fun FC(render: RBuilder.(RProps) -> Unit) = FC<RProps>(render)
