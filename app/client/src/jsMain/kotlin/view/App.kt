package app.client.view

import androidx.compose.runtime.Composable
import app.client.AppContext
import app.client.view.component.Drawer
import app.client.view.component.Navbar
import app.client.view.page.AppRoutes
import app.client.view.page.search.SearchPage
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContext
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContextOpts
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarMain
import dev.petuska.kmdc.typography.mdcTypography

@Composable
fun AppContext.App() {
  MDCTopAppBarContext({ type = MDCTopAppBarContextOpts.Type.Fixed }) {
    Drawer()
    Navbar(this)
    MDCTopAppBarMain(
      attrs = {
        mdcTypography()
      }
    ) {
      AppRoutes()
    }
  }
}
