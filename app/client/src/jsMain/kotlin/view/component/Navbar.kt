package app.client.view.component

import androidx.compose.runtime.Composable
import app.client.AppContext
import app.client.util.FABIcon
import app.client.view.style.AppStyle
import dev.petuska.kmdc.top.app.bar.MDCTopAppBar
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarActionButton
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarActionLink
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarOpts
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarRow
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarSection
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarSectionOpts
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarTitle
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.right
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text

object NavbarStyle : StyleSheet(AppStyle) {
  val container by style {
    top(0.px)
    left(0.px)
    right(0.px)
  }
}

@Composable
fun AppContext.Navbar(type: MDCTopAppBarOpts.Type) {
  MDCTopAppBar(opts = { this.type = type }, attrs = {
    classes(NavbarStyle.container)
  }) {
  MDCTopAppBarRow {
    MDCTopAppBarSection {
      KampIcon()
      MDCTopAppBarTitle("KAMP")
    }
    MDCTopAppBarSection {
      CountBadge()
    }
    MDCTopAppBarSection(opts = { align = MDCTopAppBarSectionOpts.Align.End }) {
    MDCTopAppBarActionLink(attrs = {
      href("https://github.com/mpetuska/kamp")
      target(ATarget.Blank)
    }) {
    FABIcon("github")
  }
    MDCTopAppBarActionButton(attrs = { classes("material-icons") }) {
    Text("search")
  }
  }
  }
}
}

@Composable
private fun CountBadge() {
  Text("TODO: count badge")
}

@Composable
private fun KampIcon() {
  A(attrs = {
    href("#")
  }) {
  Img(
    src = "/images/kamp.svg",
    attrs = {
      style {
        width(3.cssRem)
        height(3.cssRem)
        display(DisplayStyle.Inline)
        borderRadius(50.percent)
      }
    },
  )
}
}
