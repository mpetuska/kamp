package app.client.store.reducer

import app.client.store.action.AppAction
import app.client.store.state.AppState
import org.reduxkotlin.ReducerForActionType
import org.reduxkotlin.reducerForActionType

typealias AppReducer = ReducerForActionType<AppState, AppAction>

val rootReducer = reducerForActionType<AppState, AppAction> { state, action ->
  when (action) {
    AppAction.IncrementCount -> state.copy(count = (state.count ?: 0) + 1)
    AppAction.DecrementCount -> state.copy(count = (state.count ?: 0) - 1)
    is AppAction.SetLibraries -> state.copy(libraries = action.libraries)
    is AppAction.SetSearch -> state.copy(search = action.search)
    is AppAction.SetTargets -> state.copy(targets = action.targets)
    is AppAction.SetCount -> state.copy(count = action.count)
  }
}
