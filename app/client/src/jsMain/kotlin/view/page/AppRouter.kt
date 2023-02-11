package dev.petuska.kodex.client.view.page

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.RouteBuilder
import dev.petuska.kmdc.typography.MDCBody1
import dev.petuska.kodex.client.store.AppStore
import dev.petuska.kodex.client.store.action.AppAction
import dev.petuska.kodex.client.store.state.Page
import dev.petuska.kodex.client.store.thunk.parseQuery
import dev.petuska.kodex.client.view.page.home.HomePage
import dev.petuska.kodex.client.view.page.search.SearchPage
import dev.petuska.kodex.client.view.page.statistics.StatisticsPage
import org.kodein.di.compose.rememberInstance

@Composable
fun RouteBuilder.AppRouter() {
  val store by rememberInstance<AppStore>()
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
