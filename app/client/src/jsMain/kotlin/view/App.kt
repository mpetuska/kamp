package dev.petuska.kamp.client.view

import androidx.compose.runtime.Composable
import dev.petuska.kamp.client.view.component.Drawer
import dev.petuska.kamp.client.view.component.Navbar
import dev.petuska.kamp.client.view.page.AppRouter
import dev.petuska.kmdc.top.app.bar.MDCTopAppBar
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarType
import dev.petuska.kmdc.top.app.bar.Main
import dev.petuska.kmdc.typography.mdcTypography

@Composable
fun App() {
  MDCTopAppBar(type = MDCTopAppBarType.Fixed) {
    Drawer()
    Navbar()
    Main(attrs = { mdcTypography() }) {
      AppRouter()
    }
  }
}
