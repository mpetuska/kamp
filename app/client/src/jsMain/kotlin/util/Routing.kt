package dev.petuska.kamp.client.util

import kotlinx.browser.window

object Routing {
  private fun buildQuery(parameters: Map<*, *>): String = parameters
    .filterValues { v -> v != null && !(v is String && v.isBlank()) }
    .entries
    .joinToString("&", prefix = "?") { (k, v) ->
      when (v) {
        is Array<*> -> v.joinToString("&") { "$k=$it" }
        is Collection<*> -> v.joinToString("&") { "$k=$it" }
        else -> "$k=$v"
      }
    }.takeIf { it != "?" } ?: ""

  fun setQuery(parameters: Map<String, Any?>) {
    window.location.hash = window.location.hash.split("?")[0] + buildQuery(parameters)
  }

  fun setQuery(vararg parameters: Pair<String, Any>): Unit = setQuery(mapOf(*parameters))
}
