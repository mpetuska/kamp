package dev.petuska.kodex.client.store.thunk

import dev.petuska.kodex.client.service.LibraryService
import dev.petuska.kodex.client.store.action.AppAction
import dev.petuska.kodex.client.store.state.AppState
import dev.petuska.kodex.client.util.suspending
import dev.petuska.kodex.core.domain.KotlinTarget
import org.reduxkotlin.thunk.Thunk

typealias AppThunk = Thunk<AppState>

fun LibraryService.fetchLibraryPage(
  page: Int,
  size: Int = 12,
  search: String? = null,
  targets: Set<KotlinTarget>? = null,
): AppThunk = { dispatch, getState, _ ->
  suspending {
    val state = getState()
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<*>::isNotEmpty)

    dispatch(AppAction.SetLoading(true))
    val theLibraries =
      search(
        page,
        size,
        theSearch,
        theTargets?.map(KotlinTarget::platform)?.toSet()
      ) { current, total ->
        val progress = total.toDouble() / current
        dispatch(AppAction.SetLoading(progress > 0, progress))
      }
    dispatch(AppAction.SetSearch(theSearch))
    dispatch(AppAction.SetTargets(theTargets))
    dispatch(AppAction.SetLibraries(theLibraries))
    dispatch(AppAction.SetLoading(false))
  }
}

fun parseQuery(query: String): AppThunk = { dispatch, _, _ ->
  val parameters = query.split("&")
    .map { it.split("=").let { (k, v) -> k to v } }
    .groupBy { it.first }.mapValues { (_, v) -> v.map { it.second }.toSet() }

  parameters["target"]?.let { targets ->
    val kotlinTargets = KotlinTarget.values().let {
      targets.mapNotNull { target ->
        it.find { t -> target == t.platform }
      }
    }.toSet()
    dispatch(AppAction.SetTargets(kotlinTargets))
  }

  parameters["search"].let { search ->
    dispatch(AppAction.SetSearch(search?.joinToString("")))
  }

  Unit
}

fun LibraryService.fetchLibraryCount(
  search: String? = null,
  targets: Set<KotlinTarget>? = null,
): AppThunk = { dispatch, getState, _ ->
  suspending {
    val state = getState()
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<*>::isNotEmpty)

    val theCount = count(theSearch, theTargets?.map(KotlinTarget::toString)?.toSet()).count

    dispatch(AppAction.SetCount(theCount))
    dispatch(AppAction.SetSearch(theSearch))
    dispatch(AppAction.SetTargets(theTargets))
  }
}
