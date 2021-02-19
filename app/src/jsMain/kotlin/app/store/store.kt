package app.store

import app.store.reducer.*
import app.store.state.*
import io.kvision.redux.*
import org.reduxkotlin.*

val store = createReduxStore(appReducer, AppState(), createThunkMiddleware())
