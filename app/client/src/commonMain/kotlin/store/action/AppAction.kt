package dev.petuska.kamp.client.store.action

import dev.petuska.kamp.client.store.state.Page
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.KotlinTarget
import dev.petuska.kamp.core.domain.PagedResponse

sealed class AppAction {
  object ToggleDrawer : AppAction()
  data class SetDrawer(val isOpen: Boolean) : AppAction()
  data class SetLibraries(val libraries: PagedResponse<KotlinLibrary>?) : AppAction()
  data class SetSearch(val search: String?) : AppAction()
  data class SetTargets(val targets: Set<KotlinTarget>?) : AppAction()
  data class AddTargets(val targets: Set<KotlinTarget>) : AppAction() {
    constructor(target: KotlinTarget) : this(setOf(target))
  }

  data class RemoveTargets(val targets: Set<KotlinTarget>) : AppAction() {
    constructor(target: KotlinTarget) : this(setOf(target))
  }

  data class SetCount(val count: Long?) : AppAction()
  data class SetLoading(val loading: Boolean, val progress: Number? = null) : AppAction()
  data class SetPage(val page: Page) : AppAction()
}