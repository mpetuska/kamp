package app.client.store.state

import app.client.config.AppEnv
import domain.KotlinMPPLibrary
import domain.PagedResponse

data class AppState(
  val env: AppEnv,
  val page:Page = Page.Home,
  val count: Long? = null,
  val libraries: PagedResponse<KotlinMPPLibrary>? = null,
  val search: String? = null,
  val targets: Set<String>? = null,
  val drawerOpen: Boolean = false,
  val progress: Number? = null,
  val loading: Boolean = false,
)

enum class Page {
  Home, Search, Statistics, Random;

  val route: String = name.lowercase()
  override fun toString(): String {
    return route
  }
}
