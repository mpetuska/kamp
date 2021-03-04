package app.store.thunk

import app.config.*
import app.service.*
import app.store.action.*
import app.store.state.*
import app.util.*
import io.ktor.http.*
import io.kvision.redux.*
import kotlinx.browser.*
import org.kodein.di.*


fun fetchLibraryPage(page: Int, size: Int = 20, search: String? = null): ActionCreator<AppAction, AppState> = { dispatch, getState ->
  val theSearch = search ?: getState().search
  if (search != null) {
    dispatch(AppAction.SetSearch(search.takeIf { it.isNotBlank() }))
  }
  val service by di.instance<LibraryService>()
  suspending {
    val libraries = service.getAll(page, size, theSearch)
    dispatch(AppAction.SetLibraries(libraries))
    window.scrollTo(0.0, 0.0)
  }
}

fun fetchLibraryPage(url: String?): ActionCreator<AppAction, AppState> = { dispatch, getState ->
  if (url != null) {
    val params = Url(url).parameters
    fetchLibraryPage(params["page"]!!.toInt(), params["size"]!!.toInt())(dispatch, getState)
  }
}

fun fetchLibraryCount(): ActionCreator<AppAction, AppState> = { dispatch, _ ->
  val service by di.instance<LibraryService>()
  suspending {
    dispatch(AppAction.SetLibraryCount(service.getCount().count))
  }
}
