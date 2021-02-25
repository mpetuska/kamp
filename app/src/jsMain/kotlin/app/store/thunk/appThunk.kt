package app.store.thunk

import app.config.*
import app.service.*
import app.store.action.*
import app.store.state.*
import app.util.*
import io.ktor.http.*
import io.kvision.redux.*
import org.kodein.di.*


fun fetchLibraryPage(url: String?): ActionCreator<AppAction, AppState> = { dispatch, _ ->
  if (url != null) {
    val service by di.instance<LibraryService>()
    val params = Url(url).parameters
    suspending {
      dispatch(AppAction.SetLibraries(service.getAll(params["page"]!!.toInt(), params["size"]!!.toInt())))
    }
  }
}
