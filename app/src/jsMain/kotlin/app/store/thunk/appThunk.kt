package app.store.thunk

import app.config.di
import app.service.LibraryService
import app.store.LibraryStore
import kotlinx.browser.window
import org.kodein.di.instance

fun fetchLibraryPage(page: Int, size: Int = 12, search: String? = null, targets: Set<String>? = null) =
  LibraryStore.handle { state ->
    val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
    val theTargets = (targets ?: state.targets)?.takeIf(Set<String>::isNotEmpty)

    val service by di.instance<LibraryService>()
    val libraries = service.getAll(page, size, theSearch, theTargets)
    window.scrollTo(0.0, 0.0)
    state.copy(
      libraries = libraries,
      search = theSearch,
      targets = theTargets,
    )
  }

fun fetchLibraryCount(search: String? = null, targets: Set<String>? = null) = LibraryStore.handle { state ->
  val theSearch = (search ?: state.search)?.takeIf(String::isNotEmpty)
  val theTargets = (targets ?: state.targets)?.takeIf(Set<String>::isNotEmpty)

  val service by di.instance<LibraryService>()
  val count = service.getCount(theSearch, theTargets).count
  state.copy(
    count = count,
    search = theSearch,
    targets = theTargets,
  )
}
