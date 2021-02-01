package app.service

import kamp.domain.*

object LibraryService {
  suspend fun getAll(page: Int, size: Int): PagedResponse<KotlinMPPLibrary> {
    return PagedResponse(listOf(
      KotlinMPPLibrary("lt.petuska",
        "kamp",
        "1.0.0",
        setOf(),
        "Test Lib",
        "www.kamp.ml",
        "https://github.com/mpetuska/kamp.git")
    ), page + 1, size, 10)
  }
}
