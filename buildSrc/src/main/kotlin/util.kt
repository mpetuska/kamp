import groovy.lang.*

typealias Lambda<R, V> = R.() -> V

fun <R, V> Lambda<R, V>.toClosure(owner: Any? = null, thisObj: Any? = null) = object : Closure<V>(owner, thisObj) {
  @Suppress("UNCHECKED_CAST")
  fun doCall() {
    with(delegate as R) {
      this@toClosure()
    }
  }
}

fun <R, V> closureOf(owner: Any? = null, thisObj: Any? = null, func: R.() -> V) = func.toClosure(owner, thisObj)
