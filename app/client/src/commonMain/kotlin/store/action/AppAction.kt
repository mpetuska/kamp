package app.client.store.action

import domain.KotlinMPPLibrary
import domain.PagedResponse

sealed class AppAction {
  object IncrementCount : AppAction()
  object DecrementCount : AppAction()
  object ToggleDrawer : AppAction()
  data class SetDrawer(val isOpen: Boolean) : AppAction()
  data class SetLibraries(val libraries: PagedResponse<KotlinMPPLibrary>?) : AppAction()
  data class SetSearch(val search: String?) : AppAction()
  data class SetTargets(val targets: Set<String>?) : AppAction()
  data class SetCount(val count: Long?) : AppAction()
}
