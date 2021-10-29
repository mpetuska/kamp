package dev.petuska.kamp.client.store

import dev.petuska.kamp.client.config.AppEnv
import dev.petuska.kamp.client.store.reducer.loadReducer
import dev.petuska.kamp.client.store.state.AppState
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
