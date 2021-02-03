package app.util

import io.ktor.http.*
import io.ktor.request.*

fun ApplicationRequest.buildNextUrl(total: Int): String? = if (page * pageSize > total) {
  URLBuilder(call.request.uri).apply {
    parameters["page"] = "${page + 1}"
    parameters["size"] = "$pageSize"
  }.buildString()
} else {
  null
}

fun ApplicationRequest.buildPrevUrl(): String? = if (page > 1) {
  URLBuilder(call.request.uri).apply {
    parameters["page"] = "${page - 1}"
    parameters["size"] = "$pageSize"
  }.buildString()
} else {
  null
}

val ApplicationRequest.page get() = queryParameters["page"]?.let(String::toIntOrNull) ?: 1
val ApplicationRequest.pageSize get() = queryParameters["size"]?.let(String::toIntOrNull) ?: 50
