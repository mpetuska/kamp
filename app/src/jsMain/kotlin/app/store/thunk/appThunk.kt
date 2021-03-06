package app.store.thunk

import app.config.*
import app.service.*
import app.store.*
import kotlinx.browser.*
import org.kodein.di.*


fun fetchLibraryPage(page: Int, size: Int = 20, search: String? = null) = LibraryStore.handle { state ->
  val theSearch = search ?: state.search
  val service by di.instance<LibraryService>()
  val libraries = service.getAll(page, size, theSearch)
  window.scrollTo(0.0, 0.0)
  state.copy(
    libraries = libraries,
    search = search,
  )
}

fun fetchLibraryCount() = LibraryStore.handle { state ->
  val service by di.instance<LibraryService>()
  val count = service.getCount().count
  state.copy(count = count)
}
