package app

import app.config.*
import app.store.thunk.*
import app.view.*
import dev.fritz2.dom.html.*
import kotlinx.coroutines.*

suspend fun main() = coroutineScope {
  loadEnv()
  launch {
    fetchLibraryPage(1)(Unit)
    fetchLibraryCount()(Unit)
  }
  render("#root") {
    App()
  }
}
