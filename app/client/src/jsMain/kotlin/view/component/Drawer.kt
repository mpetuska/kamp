package app.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.store.state.Page
import app.client.util.select
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

@Composable
fun AppContext.Drawer() {
  val open by select { drawerOpen }

  MDCDrawer(
    opts = {
      type = MDCDrawerOpts.Type.Modal
      isOpen = open
    },
    attrs = {
      mdcTypography()
      addEventListener("MDCDrawer:opened") {
        dispatch(AppAction.SetDrawer(true))
      }
      addEventListener("MDCDrawer:closed") {
        dispatch(AppAction.SetDrawer(false))
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
fun AppContext.PageList(vararg pages: Page) {
  val current by select { page }
  MDCNavList(
    opts = { singleSelection = true },
    attrs = {
      addEventListener("MDCList:action") {
        dispatch(AppAction.SetDrawer(false))
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
