package service

import app.client.util.UrlUtils
import domain.KotlinMPPLibrary
import domain.LibraryCount
import domain.PagedResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.onDownload
import io.ktor.client.request.get
import service.LibraryService.Companion.PATH

class LibraryServiceImpl(private val client: HttpClient, private val urlUtils: UrlUtils) : LibraryService {
  private fun String.toApiUrl() = with(urlUtils) { toApiUrl() }

  override suspend fun getAll(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
    onProgress: (suspend (current: Long, total: Long) -> Unit)?,
  ): PagedResponse<KotlinMPPLibrary> {
    val pagination = "page=$page&size=$size"
    val searchQuery = search?.let { "search=$it" } ?: ""
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=") ?: ""

    return client.get("${PATH}${buildQuery(pagination, searchQuery, targetsQuery)}".toApiUrl()) {
      onDownload(onProgress)
    }
  }

  override suspend fun create(library: KotlinMPPLibrary) {
    TODO("Not yet implemented")
  }

  override suspend fun getCount(search: String?, targets: Set<String>?): LibraryCount {
    val searchQuery = search?.let { "search=$it" }
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=")

    return client.get("$PATH/count${buildQuery(searchQuery, targetsQuery)}".toApiUrl())
  }

  private fun buildQuery(vararg query: String?): String {
    return query.toSet().filterNotNull().takeIf(List<String>::isNotEmpty)?.joinToString("&", prefix = "?") ?: ""
  }
}
