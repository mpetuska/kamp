package dev.petuska.kmdc.typography

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import dev.petuska.kmdc.MDCDsl
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H6
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

@JsModule("@material/typography/dist/mdc.typography.css")
private external val MDCTypographyCSS: dynamic

@MDCDsl
@Composable
fun MDCTypography() {
  val clazz = "mdc-typography"
  DisposableEffect(clazz) {
    document.body!!.classList.add(clazz)
    onDispose {
      document.body!!.classList.remove(clazz)
    }
  }
}

@MDCDsl
@Composable
private fun render(
  style: String,
  text: String,
  composable: @Composable (
    attrs: AttrBuilderContext<out HTMLElement>?,
    content: ContentBuilder<out HTMLElement>?
  ) -> Unit,
) {
  MDCTypographyCSS
  composable({ classes("mdc-typography--$style") }) { Text(text) }
}

@MDCDsl
@Composable
fun MDCH1(text: String) = render("headline1", text) { a, c -> H1(a, c) }

@MDCDsl
@Composable
fun MDCH2(text: String) = render("headline2", text) { a, c -> H6(a, c) }

@MDCDsl
@Composable
fun MDCH3(text: String) = render("headline3", text) { a, c -> H6(a, c) }

@MDCDsl
@Composable
fun MDCH4(text: String) = render("headline4", text) { a, c -> H6(a, c) }

@MDCDsl
@Composable
fun MDCH5(text: String) = render("headline5", text) { a, c -> H6(a, c) }

@MDCDsl
@Composable
fun MDCH6(text: String) = render("headline6", text) { a, c -> H6(a, c) }

@MDCDsl
@Composable
fun MDCSubtitle1(text: String) = render("subtitle1", text) { a, c -> H6(a, c) }

@MDCDsl
@Composable
fun MDCSubtitle2(text: String) = render("subtitle2", text) { a, c -> H6(a, c) }

@MDCDsl
@Composable
fun MDCBody1(text: String) = render("body1", text) { a, c -> P(a, c) }

@MDCDsl
@Composable
fun MDCBody2(text: String) = render("body2", text) { a, c -> P(a, c) }

@MDCDsl
@Composable
fun MDCCaption(text: String) = render("caption", text) { a, c -> Span(a, c) }

@MDCDsl
@Composable
fun MDCButtonText(text: String) = render("button", text) { a, c -> Span(a, c) }

@MDCDsl
@Composable
fun MDCOverline(text: String) = render("overline", text) { a, c -> Span(a, c) }
