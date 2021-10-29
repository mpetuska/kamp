package dev.petuska.kamp.client.store.thunk

import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.state.AppState
import dev.petuska.kamp.client.util.suspending
import dev.petuska.kamp.core.service.LibraryService
import org.reduxkotlin.Thunk

typealias AppThunk = Thunk<AppState>

fun LibraryService.fetchLibraryPage(
  page: Int,
  size: Int = 12,
  search: String? = null,
  targets: Set<String>? = null,
): AppThunk = { dispatch, getState, _ ->
  suspending {
    val state = getState()
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<String>::isNotEmpty)

    dispatch(AppAction.SetLoading(true))
    val theLibraries = getAll(page, size, theSearch, theTargets) { current, total ->
      dispatch(AppAction.SetLoading(true, total.toDouble() / current))
    }
    dispatch(AppAction.SetSearch(theSearch))
    dispatch(AppAction.SetTargets(theTargets))
    dispatch(AppAction.SetLibraries(theLibraries))
    dispatch(AppAction.SetLoading(false))
  }
}

fun LibraryService.fetchLibraryCount(
  search: String? = null,
  targets: Set<String>? = null,
): AppThunk = { dispatch, getState, _ ->
  suspending {
    val state = getState()
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<String>::isNotEmpty)

    val theCount = getCount(theSearch, theTargets).count

    dispatch(AppAction.SetCount(theCount))
    dispatch(AppAction.SetSearch(theSearch))
    dispatch(AppAction.SetTargets(theTargets))
  }
}
