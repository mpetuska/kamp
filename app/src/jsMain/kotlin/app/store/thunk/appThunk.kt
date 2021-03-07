package app.store.thunk

import app.config.*
import app.service.*
import app.store.*
import kotlinx.browser.*
import org.kodein.di.*


fun fetchLibraryPage(page: Int, size: Int = 12, search: String? = null, targets: Set<String>? = null) = LibraryStore.handle { state ->
  val theSearch = search ?: state.search
  val theTargets = targets ?: state.targets
  
  val service by di.instance<LibraryService>()
  val libraries = service.getAll(page, size, theSearch, targets)
  window.scrollTo(0.0, 0.0)
  state.copy(
    libraries = libraries,
    search = theSearch,
    targets = theTargets,
  )
}

fun fetchLibraryCount() = LibraryStore.handle { state ->
  val service by di.instance<LibraryService>()
  val count = service.getCount().count
  state.copy(count = count)
}
