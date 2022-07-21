package dev.petuska.kamp.client.service

import dev.petuska.kamp.client.util.UrlUtils
import dev.petuska.kamp.core.domain.Count
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.PagedResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get

class LibraryService(private val client: HttpClient, private val urlUtils: UrlUtils) {
  private fun String.toApiUrl() = with(urlUtils) { toApiUrl() }

  suspend fun search(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
    onProgress: (suspend (current: Long, total: Long) -> Unit)? = null,
  ): PagedResponse<KotlinLibrary> {
    val pagination = "page=$page&size=$size"
    val searchQuery = search?.let { "search=$it" } ?: ""
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=") ?: ""

    return client.get("/api/libraries${buildQuery(pagination, searchQuery, targetsQuery)}".toApiUrl()) {
      onDownload(onProgress)
    }.body()
  }

  suspend fun count(search: String?, targets: Set<String>?): Count {
    val searchQuery = search?.let { "search=$it" }
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=")

    return client.get("/api/libraries/count${buildQuery(searchQuery, targetsQuery)}".toApiUrl()).body()
  }

  private fun buildQuery(vararg query: String?): String {
    return query.toSet().filterNotNull().takeIf(List<String>::isNotEmpty)?.joinToString("&", prefix = "?") ?: ""
  }
}
