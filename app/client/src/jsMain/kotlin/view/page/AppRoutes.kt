package dev.petuska.kamp.client.view.page

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.HashRouter
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.state.Page
import dev.petuska.kamp.client.view.component.PageList
import dev.petuska.kamp.client.view.page.search.SearchPage
import dev.petuska.kmdc.typography.MDCBody1
import org.kodein.di.compose.rememberInstance

@Composable
fun AppRoutes() {
  val store by rememberInstance<AppStore>()
  HashRouter("/${Page.Search}") {
    route(Page.Home.route) {
      noMatch {
        store.dispatch(AppAction.SetPage(Page.Home))
        MDCBody1("Home")
        PageList(*Page.values())
      }
    }
    route(Page.Search.route) {
      noMatch {
        store.dispatch(AppAction.SetPage(Page.Search))
        SearchPage()
      }
    }
    route(Page.Statistics.route) {
      noMatch {
        store.dispatch(AppAction.SetPage(Page.Statistics))
        MDCBody1("Stats")
      }
    }
    route(Page.Random.route) {
      noMatch {
        store.dispatch(AppAction.SetPage(Page.Random))
        MDCBody1("Random")
      }
    }
  }
}
