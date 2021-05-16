package app.client.store.state

import app.common.domain.PagedResponse
import shared.domain.KotlinMPPLibrary

data class AppState(
  val count: Long? = null,
  val libraries: PagedResponse<KotlinMPPLibrary>? = null,
  val search: String? = null,
  val targets: Set<String>? = null,
)
