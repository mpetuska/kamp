package dev.petuska.kodex.client.view.page.home

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.typography.MDCBody1
import dev.petuska.kodex.client.store.state.Page
import dev.petuska.kodex.client.view.component.PageList

@Composable
fun HomePage() {
  MDCBody1("Home")
  PageList(pages = Page.values())
}
