package app.store.state

import kamp.domain.*

data class AppState(
  val libraries: List<KotlinMPPLibrary> = listOf(),
)
