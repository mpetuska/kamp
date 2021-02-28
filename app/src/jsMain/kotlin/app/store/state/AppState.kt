package app.store.state

import app.domain.*
import kamp.domain.*

data class AppState(
  val libraries: PagedResponse<KotlinMPPLibrary> = PagedResponse(listOf(), 1, null, null),
  val search: String? = null,
)
