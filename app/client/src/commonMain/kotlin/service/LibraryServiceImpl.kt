package dev.petuska.kamp.core.service

import dev.petuska.kamp.client.util.UrlUtils
import dev.petuska.kamp.core.domain.Count
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.PagedResponse
import dev.petuska.kamp.core.service.LibraryService.Companion.PATH
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get

class LibraryServiceImpl(private val client: HttpClient, private val urlUtils: UrlUtils) : LibraryService {
  private fun String.toApiUrl() = with(urlUtils) { toApiUrl() }

  override suspend fun getAll(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
    onProgress: (suspend (current: Long, total: Long) -> Unit)?,
  ): PagedResponse<KotlinLibrary> {
    val pagination = "page=$page&size=$size"
    val searchQuery = search?.let { "search=$it" } ?: ""
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=") ?: ""

    return client.get("${PATH}${buildQuery(pagination, searchQuery, targetsQuery)}".toApiUrl()) {
      onDownload(onProgress)
    }.body()
  }

  override suspend fun create(library: KotlinLibrary) {
    TODO("Not yet implemented")
  }

  override suspend fun getCount(search: String?, targets: Set<String>?): Count {
    val searchQuery = search?.let { "search=$it" }
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=")

    return client.get("$PATH/count${buildQuery(searchQuery, targetsQuery)}".toApiUrl()).body()
  }

  private fun buildQuery(vararg query: String?): String {
    return query.toSet().filterNotNull().takeIf(List<String>::isNotEmpty)?.joinToString("&", prefix = "?") ?: ""
  }
}
