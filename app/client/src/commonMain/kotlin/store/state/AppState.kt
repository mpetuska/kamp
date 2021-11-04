package dev.petuska.kamp.client.store.state

import dev.petuska.kamp.client.config.AppEnv
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.KotlinTarget
import dev.petuska.kamp.core.domain.PagedResponse

data class AppState(
  val env: AppEnv,
  val page: Page = Page.Home,
  val count: Long? = null,
  val libraries: PagedResponse<KotlinLibrary>? = null,
  val search: String? = null,
  val targets: Set<KotlinTarget>? = null,
  val drawerOpen: Boolean = false,
  val progress: Number? = null,
  val loading: Boolean = false,
)

enum class Page(val icon: String) {
  Home("home"), Search("search"), Statistics("stacked_bar_chart"), Random("casino");

  val route: String = name.lowercase()
  override fun toString(): String {
    return route
  }
}
