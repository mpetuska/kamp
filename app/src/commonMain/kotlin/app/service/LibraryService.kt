package app.service

import app.domain.*
import kamp.domain.*

expect class LibraryService {
  suspend fun getAll(page: Int, size: Int = 20, search: String? = null, targets: Set<String>? = null): PagedResponse<KotlinMPPLibrary>
  suspend fun getCount(search: String? = null, targets: Set<String>? = null): LibraryCount
  
  companion object
}

val LibraryService.Companion.path get() = "/api/libraries"
