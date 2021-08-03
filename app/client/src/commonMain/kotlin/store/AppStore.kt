package app.client.store

import app.client.config.AppEnv
import app.client.store.reducer.loadReducer
import app.client.store.state.AppState
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createStore
import org.reduxkotlin.createThunkMiddleware

typealias AppStore = Store<AppState>

fun loadStore(env: AppEnv) = createStore(
  loadReducer(),
  AppState(env),
  applyMiddleware(
    createThunkMiddleware()
  )
)
