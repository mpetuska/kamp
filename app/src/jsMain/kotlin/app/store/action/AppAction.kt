package app.store.action

import app.domain.*
import app.store.state.*
import io.kvision.redux.*
import kamp.domain.*

sealed class AppAction : RAction {
  data class ResetState(val state: AppState) : AppAction()
  data class SetLibraries(val libraries: PagedResponse<KotlinMPPLibrary>) : AppAction()
  data class SetLibraryCount(val count: Long?) : AppAction()
  data class SetSearch(val search: String?) : AppAction()
}
