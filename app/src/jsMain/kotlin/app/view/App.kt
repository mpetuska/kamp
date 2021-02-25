package app.view

import app.config.*
import app.service.*
import app.store.*
import app.store.action.*
import app.store.state.*
import app.util.*
import io.kvision.*
import io.kvision.panel.*
import org.kodein.di.*


class App : Application() {
  init {
    val service by di.instance<LibraryService>()
    store.dispatch(suspending { dispatch, _ ->
      dispatch(AppAction.SetLibraries(service.getAll(1)))
    })
  }
  
  override fun start(state: Map<String, Any>) {
    state["state"]?.let {
      store.dispatch(AppAction.ResetState(it.unsafeCast<AppState>()))
    }
    root("root", ContainerType.NONE, false) {
      Header()
      simplePanel(classes = setOf("container")) {
        Content()
      }
    }
  }
  
  override fun dispose(): Map<String, Any> {
    return mapOf("state" to store.getState())
  }
}
