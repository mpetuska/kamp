package app.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.util.FABIcon
import app.client.util.select
import app.client.view.style.AppStyle
import dev.petuska.kmdc.top.app.bar.MDCTopAppBar
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarActionLink
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContextScope
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarNavigationButton
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarRow
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarSection
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarSectionOpts
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarTitle
import dev.petuska.kmdc.typography.mdcTypography
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
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
  val title by style {
    textDecoration("underline")
  }
  val logo by style {
    width(3.cssRem)
    height(3.cssRem)
    display(DisplayStyle.Inline)
    borderRadius(50.percent)
  }
}

@Composable
fun AppContext.Navbar(scope: MDCTopAppBarContextScope) {
  val drawerOpen by select { drawerOpen }
  scope.MDCTopAppBar(
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
            dispatch(AppAction.ToggleDrawer)
          }
        }) {
          Text(if (drawerOpen) "close" else "search")
        }
      }
      MDCTopAppBarSection(
        opts = { align = MDCTopAppBarSectionOpts.Align.Start },
        attrs = {
          onClick {
            window.location.href = "#"
          }
          classes(NavbarStyle.brand)
        }
      ) {
        KampIcon()
        MDCTopAppBarTitle("KAMP", attrs = { classes(NavbarStyle.title) })
      }
      MDCTopAppBarSection(opts = { align = MDCTopAppBarSectionOpts.Align.End }) {
        CountBadge()
        MDCTopAppBarActionLink(
          attrs = {
            href("https://github.com/mpetuska/kamp")
            target(ATarget.Blank)
          }
        ) {
          FABIcon("github")
        }
      }
    }
  }
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
