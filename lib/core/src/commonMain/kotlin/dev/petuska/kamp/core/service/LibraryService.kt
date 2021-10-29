package dev.petuska.kamp.core.service

import dev.petuska.kamp.core.domain.KotlinMPPLibrary
import dev.petuska.kamp.core.domain.LibraryCount
import dev.petuska.kamp.core.domain.PagedResponse

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
    const val PATH = "/api/libraries"
  }
}
