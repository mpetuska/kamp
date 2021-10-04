package app.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.util.select
import dev.petuska.kmdc.drawer.MDCDrawer
import dev.petuska.kmdc.drawer.MDCDrawerContent
import dev.petuska.kmdc.drawer.MDCDrawerHeader
import dev.petuska.kmdc.drawer.MDCDrawerOpts
import dev.petuska.kmdc.drawer.MDCDrawerScrim
import dev.petuska.kmdc.drawer.MDCDrawerSubtitle
import dev.petuska.kmdc.drawer.MDCDrawerTitle
import dev.petuska.kmdc.list.MDCList
import dev.petuska.kmdc.list.MDCListItem
import dev.petuska.kmdc.list.MDCListItemText
import dev.petuska.kmdc.list.MDCListModule
import dev.petuska.kmdc.typography.mdcTypography

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
      PageList("Search", "Home", "Search", "Random")
    }
  }
  MDCDrawerScrim()
}

@Composable
private fun AppContext.PageList(current: String, vararg pages: String) {
  MDCList(attrs = {
    addEventListener("MDCList:action") {
      val nextPage = it.nativeEvent.unsafeCast<MDCListModule.MDCListActionEvent>().detail.index
      dispatch(AppAction.SetDrawer(false))
      println("Next Page: ${pages[nextPage]}")
    }
  }) {
    pages.forEachIndexed { i, page ->
      MDCListItem(
        opts = {
          selected = page == current
        },
        attrs = {
          tabIndex(i)
        }
      ) {
        MDCListItemText(page)
      }
    }
  }
}
