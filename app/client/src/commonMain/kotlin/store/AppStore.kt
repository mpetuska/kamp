package app.client.store

import app.client.store.reducer.rootReducer
import app.client.store.state.AppState
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createStore
import org.reduxkotlin.createThunkMiddleware

typealias AppStore = Store<AppState>

val appStore: AppStore by lazy {
  createStore(
    rootReducer,
    AppState(),
    applyMiddleware(
      createThunkMiddleware()
    )
  )
}
