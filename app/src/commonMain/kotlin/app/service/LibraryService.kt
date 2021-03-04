package app.service

import app.domain.*
import kamp.domain.*

expect class LibraryService {
  suspend fun getAll(page: Int, size: Int = 20, search: String? = null): PagedResponse<KotlinMPPLibrary>
  suspend fun getCount(): LibraryCount
  
  companion object
}

val LibraryService.Companion.path get() = "/api/libraries"
