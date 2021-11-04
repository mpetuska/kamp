package dev.petuska.kamp.client.view.page.home

import androidx.compose.runtime.Composable
import dev.petuska.kamp.client.store.state.Page
import dev.petuska.kamp.client.view.component.PageList
import dev.petuska.kmdc.typography.MDCBody1

@Composable
fun HomePage() {
  MDCBody1("Home")
  PageList(*Page.values())
}
