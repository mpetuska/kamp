package service

import app.client.util.toApi
import domain.KotlinMPPLibrary
import domain.LibraryCount
import domain.PagedResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import service.LibraryService.Companion.PATH

class LibraryServiceImpl(private val client: HttpClient) : LibraryService {
  override suspend fun getAll(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
  ): PagedResponse<KotlinMPPLibrary> {
    val pagination = "page=$page&size=$size"
    val searchQuery = search?.let { "search=$it" } ?: ""
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=") ?: ""

    return client.get("${PATH}${buildQuery(pagination, searchQuery, targetsQuery)}".toApi())
  }

  override suspend fun create(library: KotlinMPPLibrary) {
    TODO("Not yet implemented")
  }

  override suspend fun getCount(search: String?, targets: Set<String>?): LibraryCount {
    val searchQuery = search?.let { "search=$it" }
    val targetsQuery = targets?.joinToString(prefix = "target=", separator = "&target=")

    return client.get("$PATH/count${buildQuery(searchQuery, targetsQuery)}".toApi())
  }

  private fun buildQuery(vararg query: String?): String {
    return query.toSet().filterNotNull().takeIf(List<String>::isNotEmpty)?.joinToString("&", prefix = "?") ?: ""
  }
}
