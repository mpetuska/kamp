package app.client.store.thunk

import app.client.config.di
import app.client.store.action.AppAction
import app.client.store.state.AppState
import app.client.util.suspending
import org.kodein.di.instance
import org.reduxkotlin.Thunk
import service.LibraryService

typealias AppThunk = Thunk<AppState>

fun fetchLibraryPage(
  page: Int,
  size: Int = 12,
  search: String? = null,
  targets: Set<String>? = null,
): AppThunk = { dispatch, getState, _ ->
  suspending {
    val state = getState()
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<String>::isNotEmpty)

    val service by di.instance<LibraryService>()
    val theLibraries = service.getAll(page, size, theSearch, theTargets)
    // TODO window.scrollTo(0.0, 0.0)
    dispatch(AppAction.SetLibraries(theLibraries))
    dispatch(AppAction.SetSearch(theSearch))
    dispatch(AppAction.SetTargets(theTargets))
  }
}

fun fetchLibraryCount(
  search: String? = null,
  targets: Set<String>? = null,
): AppThunk = { dispatch, getState, _ ->
  suspending {
    val state = getState()
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<String>::isNotEmpty)

    val service by di.instance<LibraryService>()
    val theCount = service.getCount(theSearch, theTargets).count

    dispatch(AppAction.SetCount(theCount))
    dispatch(AppAction.SetSearch(theSearch))
    dispatch(AppAction.SetTargets(theTargets))
  }
}
