package dev.petuska.kamp.client.view.page

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.HashRouter
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.state.Page
import dev.petuska.kamp.client.store.thunk.parseQuery
import dev.petuska.kamp.client.view.page.home.HomePage
import dev.petuska.kamp.client.view.page.search.SearchPage
import dev.petuska.kamp.client.view.page.statistics.StatisticsPage
import dev.petuska.kmdc.typography.MDCBody1
import org.kodein.di.compose.rememberInstance

@Composable
fun AppRouter() {
  val store by rememberInstance<AppStore>()
  HashRouter("/${Page.Home}") {
    string { hash ->
      val chunks = hash.split("?")
      val route = chunks[0]
      val rootRoute = route.split("/")[0]
      chunks.getOrNull(1)?.also {
        store.dispatch(parseQuery(it))
      }

      val page = Page.values().find { it.route == rootRoute }?.also {
        store.dispatch(AppAction.SetPage(it))
      }
      when (page) {
        Page.Home -> HomePage()
        Page.Search -> SearchPage()
        Page.Statistics -> StatisticsPage()
        Page.Random -> MDCBody1("Random")
        null -> println("null")
//        null -> router.navigate("/${Page.Home}")
      }
    }
  }
}
