package app.store.reducer

import app.store.action.*
import app.store.state.*
import org.reduxkotlin.*


val appReducer: ReducerForActionType<AppState, AppAction> = { state, action ->
  when (action) {
    is AppAction.ResetState -> action.state
    is AppAction.SetLibraries -> state.copy(libraries = action.libraries)
    is AppAction.SetSearch -> state.copy(search = action.search)
    is AppAction.SetLibraryCount -> state.copy(libraryCount = action.count)
  }
}
