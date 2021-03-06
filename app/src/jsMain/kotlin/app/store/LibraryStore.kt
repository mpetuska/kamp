package app.store

import app.domain.*
import dev.fritz2.binding.*
import kamp.domain.*


object LibraryStore : RootStore<LibraryStore.LibraryState>(LibraryState()) {
  data class LibraryState(
    val libraries: PagedResponse<KotlinMPPLibrary>? = null,
    val search: String? = null,
    val targets: Set<String>? = null,
    val count: Long? = null,
  )
  
  val setLibraries = handleAndEmit<PagedResponse<KotlinMPPLibrary>, PagedResponse<KotlinMPPLibrary>> { state, libraries ->
    emit(libraries)
    state.copy(libraries = libraries)
  }
  
  val setSearch = handleAndEmit<String?, String?> { state, search ->
    emit(search)
    state.copy(search = search)
  }
  
  val setCount = handleAndEmit<Long?, Long?> { state, count ->
    emit(count)
    state.copy(count = count)
  }
}
