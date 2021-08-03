package app.client.store.state

import domain.KotlinMPPLibrary
import domain.PagedResponse

data class AppState(
  val count: Long? = null,
  val libraries: PagedResponse<KotlinMPPLibrary>? = null,
  val search: String? = null,
  val targets: Set<String>? = null,
)
