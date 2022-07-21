package dev.petuska.kamp.server.util

import dev.petuska.kamp.core.domain.PagedResponse
import dev.petuska.kamp.core.util.buildIf
import io.ktor.http.URLBuilder
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.port
import io.ktor.server.request.uri

fun ApplicationRequest.buildNextUrl(currentElementCount: Int): String? = buildIf(currentElementCount == pageSize) {
  URLBuilder(call.request.uri)
    .apply {
      port = call.request.port()
      parameters["page"] = "${page + 1}"
      parameters["size"] = "$pageSize"
    }.buildString()
}

fun ApplicationRequest.buildPrevUrl(): String? = buildIf(page > 1) {
  URLBuilder(call.request.uri)
    .apply {
      port = call.request.port()
      parameters["page"] = "${page - 1}"
      parameters["size"] = "$pageSize"
    }.buildString()
}

fun <T> ApplicationRequest.paginateResponse(data: List<T>) = PagedResponse(
  data = data,
  prev = buildPrevUrl(),
  page = page,
  next = buildNextUrl(data.size)
)

val ApplicationRequest.page
  get() = queryParameters["page"]?.let(String::toIntOrNull) ?: 1

val ApplicationRequest.pageSize
  get() = queryParameters["size"]?.let(String::toIntOrNull) ?: 50

val ApplicationRequest.search
  get() = queryParameters["search"]?.takeIf { it.isNotBlank() }

val ApplicationRequest.targets
  get() = queryParameters.getAll("target")?.toSet()?.takeIf { it.isNotEmpty() }

val ApplicationRequest.from
  get() = queryParameters["from"]?.let(String::toLongOrNull)

val ApplicationRequest.to
  get() = queryParameters["to"]?.let(String::toLongOrNull)
