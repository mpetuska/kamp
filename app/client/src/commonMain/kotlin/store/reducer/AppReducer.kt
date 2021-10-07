package app.client.store.reducer

import app.client.store.action.AppAction
import app.client.store.state.AppState
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
    is AppAction.AddTarget -> state.copy(targets = (state.targets ?: setOf()) + action.target)
    is AppAction.RemoveTarget -> state.copy(targets = (state.targets ?: setOf()) - action.target)
    is AppAction.SetPage -> state.copy(page = action.page)
  }
}
