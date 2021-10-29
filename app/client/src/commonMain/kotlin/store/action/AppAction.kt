package dev.petuska.kamp.client.store.action

import dev.petuska.kamp.client.store.state.Page
import dev.petuska.kamp.core.domain.KotlinMPPLibrary
import dev.petuska.kamp.core.domain.PagedResponse

sealed class AppAction {
  object ToggleDrawer : AppAction()
  data class SetDrawer(val isOpen: Boolean) : AppAction()
  data class SetLibraries(val libraries: PagedResponse<KotlinMPPLibrary>?) : AppAction()
  data class SetSearch(val search: String?) : AppAction()
  data class SetTargets(val targets: Set<String>?) : AppAction()
  data class AddTarget(val target: String) : AppAction()
  data class RemoveTarget(val target: String) : AppAction()
  data class SetCount(val count: Long?) : AppAction()
  data class SetLoading(val loading: Boolean, val progress: Number? = null) : AppAction()
  data class SetPage(val page: Page) : AppAction()
}
