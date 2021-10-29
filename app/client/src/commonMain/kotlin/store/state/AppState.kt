package dev.petuska.kamp.client.store.state

import dev.petuska.kamp.client.config.AppEnv
import dev.petuska.kamp.core.domain.KotlinMPPLibrary
import dev.petuska.kamp.core.domain.PagedResponse

data class AppState(
  val env: AppEnv,
  val page: Page = Page.Home,
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
