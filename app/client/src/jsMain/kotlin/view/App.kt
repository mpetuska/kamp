package dev.petuska.kamp.client.view

import androidx.compose.runtime.Composable
import dev.petuska.kamp.client.view.component.Drawer
import dev.petuska.kamp.client.view.component.Navbar
import dev.petuska.kamp.client.view.page.AppRouter
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContext
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContextOpts
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarMain
import dev.petuska.kmdc.typography.mdcTypography

@Composable
fun App() {
  MDCTopAppBarContext({ type = MDCTopAppBarContextOpts.Type.Fixed }) {
    Drawer()
    Navbar()
    MDCTopAppBarMain(
      attrs = {
        mdcTypography()
      }
    ) {
      AppRouter()
    }
  }
}
