package app.service

import app.domain.LibraryCount
import app.domain.PagedResponse
import app.util.toApi
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kamp.domain.KotlinMPPLibrary

actual class LibraryService(private val client: HttpClient) {
  actual suspend fun getAll(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
  ): PagedResponse<KotlinMPPLibrary> {
    val pagination = "page=$page&size=$size"
    val searchQuery = search?.let { "search=$it" } ?: ""
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=") ?: ""

    return client.get("${path}${buildQuery(pagination, searchQuery, targetsQuery)}".toApi())
  }

  actual suspend fun getCount(search: String?, targets: Set<String>?): LibraryCount {
    val searchQuery = search?.let { "search=$it" }
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=")

    return client.get("$path/count${buildQuery(searchQuery, targetsQuery)}".toApi())
  }

  private fun buildQuery(vararg query: String?): String {
    return query.toSet().filterNotNull().takeIf(List<String>::isNotEmpty)?.joinToString("&", prefix = "?") ?: ""
  }

  actual companion object
}
