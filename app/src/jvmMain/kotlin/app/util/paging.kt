package app.util

import io.ktor.http.URLBuilder
import io.ktor.request.ApplicationRequest
import io.ktor.request.port
import io.ktor.request.uri

fun ApplicationRequest.buildNextUrl(currentElementCount: Int): String? = if (currentElementCount == pageSize) {
  URLBuilder(call.request.uri).apply {
    port = call.request.port()
    parameters["page"] = "${page + 1}"
    parameters["size"] = "$pageSize"
  }.buildString()
} else {
  null
}

fun ApplicationRequest.buildPrevUrl(): String? = if (page > 1) {
  URLBuilder(call.request.uri).apply {
    port = call.request.port()
    parameters["page"] = "${page - 1}"
    parameters["size"] = "$pageSize"
  }.buildString()
} else {
  null
}

val ApplicationRequest.page get() = queryParameters["page"]?.let(String::toIntOrNull) ?: 1
val ApplicationRequest.pageSize get() = queryParameters["size"]?.let(String::toIntOrNull) ?: 50
val ApplicationRequest.search get() = queryParameters["search"]?.takeIf { it.isNotBlank() }
val ApplicationRequest.targets get() = queryParameters.getAll("target")?.toSet()?.takeIf { it.isNotEmpty() }
