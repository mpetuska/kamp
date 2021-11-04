package dev.petuska.kamp.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.softwork.routingcompose.HashRouter
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.state.Page
import dev.petuska.kamp.client.util.FABIcon
import dev.petuska.kamp.client.util.select
import dev.petuska.kamp.client.view.style.AppStyle
import dev.petuska.kmdc.linear.progress.MDCLinearProgress
import dev.petuska.kmdc.top.app.bar.MDCTopAppBar
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarActionButton
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarActionLink
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContextScope
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarNavigationButton
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarRow
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarSection
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarSectionOpts
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarTitle
import dev.petuska.kmdc.typography.mdcTypography
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.right
import org.jetbrains.compose.web.css.selectors.CSSSelector
import org.jetbrains.compose.web.css.selectors.child
import org.jetbrains.compose.web.css.selectors.hover
import org.jetbrains.compose.web.css.textDecoration
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text
import org.kodein.di.compose.rememberInstance

object NavbarStyle : StyleSheet(AppStyle) {
  val container by style {
    top(0.px)
    left(0.px)
    right(0.px)
  }
  val brand by style {
    cursor("pointer")
    hover(self) style {
      child(self, CSSSelector.Universal) style {
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
  MDCTopAppBar(
    attrs = {
      classes(NavbarStyle.container)
      mdcTypography()
    }
  ) {
    MDCTopAppBarRow {
      MDCTopAppBarSection(opts = { align = MDCTopAppBarSectionOpts.Align.Start }) {
        MDCTopAppBarNavigationButton(attrs = {
          classes("material-icons")
          onClick {
            store.dispatch(AppAction.ToggleDrawer)
          }
        }) {
          Text(if (drawerOpen) "close" else "menu")
        }
        val title = remember(page) {
          "KAMP" + if (page != Page.Home) " | ${page.name.uppercase()}" else ""
        }
        MDCTopAppBarTitle(title)
        // KampIcon()
      }
      MDCTopAppBarSection(opts = { align = MDCTopAppBarSectionOpts.Align.End }) {
        CountBadge()
        MDCTopAppBarActionButton(attrs = {
          classes("material-icons")
          onClick { HashRouter.navigate("/${Page.Home}") }
        }) {
          Text("home")
        }
        MDCTopAppBarActionLink(
          attrs = {
            href("https://github.com/mpetuska/kamp")
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
    {
      indeterminate = loading && progress == null
      closed = !loading
      this.progress = progress
    }
  )
}

@Composable
private fun CountBadge() {
  Text("TODO")
}

@Composable
private fun KampIcon() {
  Img(
    src = "/images/kamp.svg",
    attrs = {
      classes(NavbarStyle.logo)
    },
  )
}
