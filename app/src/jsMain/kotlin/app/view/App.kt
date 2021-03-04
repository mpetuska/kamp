package app.view

import app.store.*
import app.store.action.*
import app.store.state.*
import app.store.thunk.*
import io.kvision.*
import io.kvision.panel.*


class App : Application() {
  override fun start(state: Map<String, Any>) {
    state["state"]?.let {
      store.dispatch(AppAction.ResetState(it.unsafeCast<AppState>()))
    } ?: run {
      store.dispatch(fetchLibraryPage(1))
      store.dispatch(fetchLibraryCount())
    }
    root("root", ContainerType.NONE, false) {
      Header()
      Content()
    }
  }
  
  override fun dispose(): Map<String, Any> {
    return mapOf("state" to store.getState())
  }
}
