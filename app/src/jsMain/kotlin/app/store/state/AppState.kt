package app.store.state

import app.domain.*
import kamp.domain.*

data class AppState(
  val libraries: PagedResponse<KotlinMPPLibrary> = PagedResponse(listOf(), null, null),
)
