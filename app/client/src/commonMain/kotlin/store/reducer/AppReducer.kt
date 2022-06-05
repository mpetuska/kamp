package dev.petuska.kamp.client.store.reducer

import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.state.AppState
import org.reduxkotlin.ReducerForActionType
import org.reduxkotlin.reducerForActionType

typealias AppReducer = ReducerForActionType<AppState, AppAction>

fun loadReducer() = reducerForActionType<AppState, AppAction> { state, action ->
  when (action) {
    is AppAction.SetLibraries -> state.copy(libraries = action.libraries)
    is AppAction.SetSearch -> state.copy(search = action.search)
    is AppAction.SetTargets -> state.copy(targets = action.targets)
    is AppAction.SetCount -> state.copy(count = action.count)

    is AppAction.ToggleDrawer -> state.copy(drawerOpen = !state.drawerOpen)
    is AppAction.SetDrawer -> state.copy(drawerOpen = action.isOpen)
    is AppAction.SetLoading -> state.copy(
      loading = action.loading,
      progress = action.progress.takeIf { action.loading },
    )
    is AppAction.AddTargets -> state.copy(targets = action.targets + (state.targets ?: setOf()))
    is AppAction.RemoveTargets -> state.copy(targets = (state.targets ?: setOf()) - action.targets)
    is AppAction.SetPage -> state.copy(page = action.page)
  }
}