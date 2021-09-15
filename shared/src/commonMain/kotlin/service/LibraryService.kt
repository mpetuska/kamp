package service

import domain.KotlinMPPLibrary
import domain.LibraryCount
import domain.PagedResponse

interface LibraryService {
  suspend fun getCount(search: String?, targets: Set<String>?): LibraryCount

  suspend fun getAll(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
    onProgress: (suspend (current: Long, total: Long) -> Unit)? = null
  ): PagedResponse<KotlinMPPLibrary>

  suspend fun create(library: KotlinMPPLibrary)

  companion object {
    const val PATH = "/libraries"
  }
}
