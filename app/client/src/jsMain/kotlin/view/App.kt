package dev.petuska.kodex.client.view

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.top.app.bar.MDCTopAppBar
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarType
import dev.petuska.kmdc.top.app.bar.Main
import dev.petuska.kmdc.typography.mdcTypography
import dev.petuska.kodex.client.view.component.Drawer
import dev.petuska.kodex.client.view.component.Navbar
import dev.petuska.kodex.client.view.page.AppRouter

@Composable
fun App() {
  MDCTopAppBar(type = MDCTopAppBarType.Fixed) {
    Navbar()
    Main(attrs = { mdcTypography() }) {
      Drawer()
      AppRouter()
    }
  }
}
