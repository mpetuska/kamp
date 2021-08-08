package dev.petuska.kmdc.layout.grid

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.Builder
import dev.petuska.kmdc.ComposableBuilder
import dev.petuska.kmdc.MDCDsl
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementScope
import org.w3c.dom.HTMLDivElement

@JsModule("@material/layout-grid/dist/mdc.layout-grid.css")
private external val MDCLayoutGridCSS: dynamic

data class MDCLayoutGridOpts(
  val columnWidth: ColumnWidth = ColumnWidth.Default,
  val align: Align = Align.Center,
) {
  enum class ColumnWidth(vararg val classes: String) {
    Default,
    Fixed("mdc-layout-grid--fixed-column-width")
  }

  enum class Align(vararg val classes: String) {
    Left("mdc-layout-grid--align-left"),
    Right("mdc-layout-grid--align-right"),
    Center
  }
}

open class MDCLayoutGridScope(scope: ElementScope<HTMLDivElement>) : ElementScope<HTMLDivElement> by scope
class MDCLayoutGridCellsScope(scope: ElementScope<HTMLDivElement>) : MDCLayoutGridScope(scope)

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-layout-grid)
 */
@MDCDsl
@Composable
fun MDCLayoutGrid(
  opts: Builder<MDCLayoutGridOpts>? = null,
  attrs: AttrBuilderContext<HTMLDivElement>? = null,
  content: ComposableBuilder<MDCLayoutGridScope>? = null
) {
  MDCLayoutGridCSS
  val options = MDCLayoutGridOpts().apply { opts?.invoke(this) }
  Div(
    attrs = {
      classes("mdc-layout-grid", *options.align.classes, *options.columnWidth.classes)
      attrs?.invoke(this)
    },
    content = content?.let { { MDCLayoutGridScope(this).it() } }
  )
}

/**
 * [JS API](https://github.com/material-components/material-components-web/tree/v12.0.0/packages/mdc-layout-grid)
 */
@MDCDsl
@Composable
fun MDCLayoutGridScope.MDCLayoutGridCells(
  attrs: AttrBuilderContext<HTMLDivElement>? = null,
  content: ComposableBuilder<MDCLayoutGridCellsScope>? = null
) {
  Div(
    attrs = {
      classes("mdc-layout-grid__inner")
      attrs?.invoke(this)
    },
    content = content?.let { { MDCLayoutGridCellsScope(this).it() } }
  )
}
