package app.client.store.thunk

import app.client.AppContext
import app.client.store.action.AppAction
import app.client.store.state.AppState
import app.client.util.suspending
import kotlinx.coroutines.delay
import org.kodein.di.instance
import org.reduxkotlin.Thunk
import service.LibraryService

typealias AppThunk = Thunk<AppState>

fun AppContext.fetchLibraryPage(
  page: Int,
  size: Int = 12,
  search: String? = null,
  targets: Set<String>? = null,
): AppThunk = { dispatch, getState, _ ->
  suspending {
    val state = getState()
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<String>::isNotEmpty)

    val service by instance<LibraryService>()
    dispatch(AppAction.SetLoading(true))
    // val theLibraries = service.getAll(page, size, theSearch, theTargets)
    delay(5 * 1000L)
    // TODO window.scrollTo(0.0, 0.0)
    // dispatch(AppAction.SetLibraries(theLibraries))
    dispatch(AppAction.SetLibraries(state.libraries))
    dispatch(AppAction.SetSearch(theSearch))
    dispatch(AppAction.SetTargets(theTargets))
    dispatch(AppAction.SetLoading(false))
  }
}

fun AppContext.fetchLibraryCount(
  search: String? = null,
  targets: Set<String>? = null,
): AppThunk = { dispatch, getState, _ ->
  suspending {
    val state = getState()
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<String>::isNotEmpty)

    val service by instance<LibraryService>()
    val theCount = service.getCount(theSearch, theTargets).count

    dispatch(AppAction.SetCount(theCount))
    dispatch(AppAction.SetSearch(theSearch))
    dispatch(AppAction.SetTargets(theTargets))
  }
}
