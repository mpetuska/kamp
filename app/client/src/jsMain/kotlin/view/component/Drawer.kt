package dev.petuska.kamp.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.state.Page
import dev.petuska.kamp.client.util.select
import dev.petuska.kmdc.drawer.MDCDrawer
import dev.petuska.kmdc.drawer.MDCDrawerContent
import dev.petuska.kmdc.drawer.MDCDrawerHeader
import dev.petuska.kmdc.drawer.MDCDrawerOpts
import dev.petuska.kmdc.drawer.MDCDrawerScrim
import dev.petuska.kmdc.drawer.MDCDrawerSubtitle
import dev.petuska.kmdc.drawer.MDCDrawerTitle
import dev.petuska.kmdc.list.MDCListItem
import dev.petuska.kmdc.list.MDCListItemText
import dev.petuska.kmdc.list.MDCNavList
import dev.petuska.kmdc.typography.mdcTypography
import org.jetbrains.compose.web.attributes.href
import org.kodein.di.compose.rememberInstance

@Composable
fun Drawer() {
  val open by select { drawerOpen }
  val store by rememberInstance<AppStore>()

  MDCDrawer(
    opts = {
      type = MDCDrawerOpts.Type.Modal
      isOpen = open
    },
    attrs = {
      mdcTypography()
      addEventListener("MDCDrawer:opened") {
        store.dispatch(AppAction.SetDrawer(true))
      }
      addEventListener("MDCDrawer:closed") {
        store.dispatch(AppAction.SetDrawer(false))
      }
    }
  ) {
    MDCDrawerHeader {
      MDCDrawerTitle("Navigation Menu")
      MDCDrawerSubtitle("Find your stuff")
    }
    MDCDrawerContent {
      PageList(*Page.values())
    }
  }
  MDCDrawerScrim()
}

@Composable
fun PageList(vararg pages: Page) {
  val current by select { page }
  val store by rememberInstance<AppStore>()
  MDCNavList(
    opts = { singleSelection = true },
    attrs = {
      addEventListener("MDCList:action") {
        store.dispatch(AppAction.SetDrawer(false))
      }
    }
  ) {
    pages.forEach { page ->
      MDCListItem({ activated = page == current }, { href("#${page.route}") }) {
        MDCListItemText(page.name)
      }
    }
  }
}
