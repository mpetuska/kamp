package dev.petuska.kodex.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.softwork.routingcompose.Router
import dev.petuska.kmdc.core.AttrsBuilder
import dev.petuska.kmdc.linear.progress.MDCLinearProgress
import dev.petuska.kmdc.top.app.bar.*
import dev.petuska.kmdc.typography.mdcTypography
import dev.petuska.kodex.client.store.AppStore
import dev.petuska.kodex.client.store.action.AppAction
import dev.petuska.kodex.client.store.state.Page
import dev.petuska.kodex.client.util.FABIcon
import dev.petuska.kodex.client.util.select
import dev.petuska.kodex.client.view.style.AppStyle
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text
import org.kodein.di.compose.rememberInstance
import org.w3c.dom.HTMLImageElement

object NavbarStyle : StyleSheet(AppStyle) {
  val container by style {
    top(0.px)
    left(0.px)
    right(0.px)
  }
  val brand by style {
    cursor("pointer")
    hover(self) style {
      child(self, universal) style {
        textDecoration("none")
      }
    }
  }

  val logoSection by style {
    display(DisplayStyle.Flex)
    flexDirection(FlexDirection.Column)
  }
  val logo by style {
    width(3.cssRem)
    height(3.cssRem)
    display(DisplayStyle.Inline)
    borderRadius(50.percent)
  }
}

@Composable
fun MDCTopAppBarContextScope.Navbar() {
  val store by rememberInstance<AppStore>()
  val drawerOpen by select { drawerOpen }
  val page by select { page }
  TopAppBar(
    attrs = {
      classes(NavbarStyle.container)
      mdcTypography()
    }
  ) {
    Row {
      Section(align = MDCTopAppBarSectionAlign.Start) {
        NavButton(attrs = {
          classes("material-icons")
          onClick {
            store.dispatch(AppAction.ToggleDrawer)
          }
        }) {
          Text(if (drawerOpen) "close" else "menu")
        }
        val title = remember(page) {
          "KODEX" + if (page != Page.Home) " | ${page.name.uppercase()}" else ""
        }
        Title(title)
        // KodexIcon()
      }
      Section(align = MDCTopAppBarSectionAlign.End) {
        val router = Router.current
        CountBadge()
        ActionButton(attrs = {
          classes("material-icons")
          onClick { router.navigate("/${Page.Home}") }
        }) {
          Text("home")
        }
        ActionLink(
          attrs = {
            href("https://github.com/mpetuska/kodex")
            target(ATarget.Blank)
            classes(AppStyle.fixFabContainer)
          }
        ) {
          FABIcon("github")
        }
      }
    }
    ProgressBar()
  }
}

@Composable
private fun ProgressBar() {
  val loading by select { loading }
  val progress by select { progress }
  MDCLinearProgress(
    determinate = loading && progress != null,
    closed = !loading,
    progress = progress ?: 0,
  )
}

@Composable
private fun CountBadge() {
  Text("TODO")
}

@Composable
@Suppress("UnusedPrivateMember")
private fun KodexIcon(attrs: AttrsBuilder<HTMLImageElement>? = null) {
  Img(
    src = "./images/kodex.svg",
    attrs = {
      classes(NavbarStyle.logo)
      attrs?.invoke(this)
    },
  )
}
