package dev.petuska.kamp.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.softwork.routingcompose.HashRouter
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
import dev.petuska.kmdc.list.MDCList
import dev.petuska.kmdc.list.MDCListItem
import dev.petuska.kmdc.list.MDCListItemGraphic
import dev.petuska.kmdc.list.MDCListItemText
import dev.petuska.kmdc.typography.mdcTypography
import org.jetbrains.compose.web.dom.Text
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
      MDCDrawerTitle("KAMP")
      MDCDrawerSubtitle("Find your stuff")
    }
    MDCDrawerContent {
      PageList(*Page.values())
    }
  }
  MDCDrawerScrim()
}

@Composable
fun PageList(vararg pages: Page, selectable: Boolean = true) {
  val current by select { page }
  val store by rememberInstance<AppStore>()
  MDCList(
    opts = { singleSelection = true },
    attrs = {
      addEventListener("MDCList:action") {
        store.dispatch(AppAction.SetDrawer(false))
        HashRouter.navigate("/${pages[it.nativeEvent.asDynamic().detail.index.unsafeCast<Int>()]}")
      }
    }
  ) {
    pages.forEach { page ->
      MDCListItem({ activated = selectable && page == current }) {
        MDCListItemGraphic(attrs = {
          classes("material-icons")
          attr("aria-hidden", "true")
        }) { Text(page.icon) }
        MDCListItemText(page.name)
      }
    }
  }
}
