package dev.petuska.kamp.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.softwork.routingcompose.Router
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.state.Page
import dev.petuska.kamp.client.util.select
import dev.petuska.kmdc.drawer.Content
import dev.petuska.kmdc.drawer.Header
import dev.petuska.kmdc.drawer.MDCDrawer
import dev.petuska.kmdc.drawer.MDCDrawerType
import dev.petuska.kmdc.drawer.Subtitle
import dev.petuska.kmdc.drawer.Title
import dev.petuska.kmdc.drawer.onClosed
import dev.petuska.kmdc.drawer.onOpened
import dev.petuska.kmdc.list.MDCList
import dev.petuska.kmdc.list.MDCListSelection
import dev.petuska.kmdc.list.item.Graphic
import dev.petuska.kmdc.list.item.ListItem
import dev.petuska.kmdc.list.item.Text
import dev.petuska.kmdc.list.onAction
import dev.petuska.kmdc.typography.mdcTypography
import org.kodein.di.compose.rememberInstance
import org.jetbrains.compose.web.dom.Text as CText

@Composable
fun Drawer() {
  val open by select { drawerOpen }
  val store by rememberInstance<AppStore>()

  MDCDrawer(
    open = open,
    type = MDCDrawerType.Modal,
    attrs = {
      mdcTypography()
      onOpened {
        store.dispatch(AppAction.SetDrawer(true))
      }
      onClosed {
        store.dispatch(AppAction.SetDrawer(false))
      }
    }
  ) {
    Header {
      Title("KAMP")
      Subtitle("Find your stuff")
    }
    Content {
      PageList(pages = Page.values())
    }
  }
}

@Composable
fun PageList(vararg pages: Page, selectable: Boolean = true) {
  val current by select { page }
  val store by rememberInstance<AppStore>()
  val router = Router.current
  MDCList(
    selection = MDCListSelection.Single,
    attrs = {
      onAction {
        store.dispatch(AppAction.SetDrawer(false))
        router.navigate("/${pages[it.detail.index]}")
      }
    }
  ) {
    pages.forEach { page ->
      ListItem(activated = selectable && page == current) {
        Graphic(attrs = {
          classes("material-icons")
          attr("aria-hidden", "true")
        }) { CText(page.icon) }
        Text(page.name)
      }
    }
  }
}
