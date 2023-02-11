package dev.petuska.kodex.client.store

import dev.petuska.kodex.client.config.AppEnv
import dev.petuska.kodex.client.store.reducer.loadReducer
import dev.petuska.kodex.client.store.state.AppState
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createStore
import org.reduxkotlin.thunk.createThunkMiddleware

typealias AppStore = Store<AppState>

fun loadStore(env: AppEnv) = createStore(
  loadReducer(),
  AppState(env),
  applyMiddleware(
    createThunkMiddleware()
  )
)
