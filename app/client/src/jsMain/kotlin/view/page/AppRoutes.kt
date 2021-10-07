package app.client.view.page

import androidx.compose.runtime.Composable
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.store.state.Page
import app.client.view.component.PageList
import app.client.view.page.search.SearchPage
import app.softwork.routingcompose.HashRouter
import dev.petuska.kmdc.typography.MDCBody1

@Composable
fun AppContext.AppRoutes() {
  HashRouter("/${Page.Search}") {
    route(Page.Home.route) {
      noMatch {
        dispatch(AppAction.SetPage(Page.Home))
        MDCBody1("Home")
        PageList(*Page.values())
      }
    }
    route(Page.Search.route) {
      noMatch {
        dispatch(AppAction.SetPage(Page.Search))
        SearchPage()
      }
    }
    route(Page.Statistics.route) {
      noMatch {
        dispatch(AppAction.SetPage(Page.Statistics))
        MDCBody1("Stats")
      }
    }
    route(Page.Random.route) {
      noMatch {
        dispatch(AppAction.SetPage(Page.Random))
        MDCBody1("Random")
      }
    }
  }
}
