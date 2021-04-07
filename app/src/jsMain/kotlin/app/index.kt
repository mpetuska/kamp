package app

import app.config.loadEnv
import app.store.thunk.fetchLibraryCount
import app.store.thunk.fetchLibraryPage
import app.view.App
import dev.fritz2.dom.html.render
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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
