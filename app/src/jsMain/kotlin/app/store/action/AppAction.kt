package app.store.action

import app.store.state.*
import io.kvision.redux.*
import kamp.domain.*

sealed class AppAction : RAction {
  data class ResetState(val state: AppState) : AppAction()
  data class SetLibraries(val libraries: List<KotlinMPPLibrary>) : AppAction()
}
