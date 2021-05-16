package app.client.store.action

import app.common.domain.PagedResponse
import shared.domain.KotlinMPPLibrary

sealed class AppAction {
  object IncrementCount : AppAction()
  object DecrementCount : AppAction()
  data class SetLibraries(val libraries: PagedResponse<KotlinMPPLibrary>?) : AppAction()
  data class SetSearch(val search: String?) : AppAction()
  data class SetTargets(val targets: Set<String>?) : AppAction()
  data class SetCount(val count: Long?) : AppAction()
}
