package app.util

import react.*
import kotlin.properties.*
import kotlin.reflect.*

class RFunction<P : RProps>(private val render: RBuilder.(P) -> Unit) : ReadOnlyProperty<Any?, RClass<P>> {
  private var ref: RClass<P>? = null
  override fun getValue(thisRef: Any?, property: KProperty<*>): RClass<P> {
    return ref ?: rFunction(property.name, render).also { ref = it }
  }
}

fun RFunction(render: RBuilder.(RProps) -> Unit) = RFunction<RProps>(render)
