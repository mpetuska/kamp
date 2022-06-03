package dev.petuska.kamp.core.service

import dev.petuska.kamp.core.domain.Count
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.PagedResponse

interface LibraryService {
  suspend fun getCount(search: String?, targets: Set<String>?): Count

  suspend fun getAll(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
    onProgress: (suspend (current: Long, total: Long) -> Unit)? = null
  ): PagedResponse<KotlinLibrary>

  suspend fun create(library: KotlinLibrary)

  companion object {
    const val PATH = "/api/libraries"
  }
}
