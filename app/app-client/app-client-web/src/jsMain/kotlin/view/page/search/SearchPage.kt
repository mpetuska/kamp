package dev.petuska.kodex.client.view.page.search

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.card.*
import dev.petuska.kmdc.layout.grid.Cell
import dev.petuska.kmdc.layout.grid.Cells
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.typography.MDCH2
import dev.petuska.kmdc.typography.MDCH4
import dev.petuska.kmdc.typography.MDCOverline
import dev.petuska.kodex.client.view.style.AppStyle
import org.jetbrains.compose.web.css.backgroundImage
import org.jetbrains.compose.web.dom.Text

@Composable
fun SearchPage() {
  MDCLayoutGrid {
    Cells {
      Cell(span = 12u, attrs = { classes(AppStyle.centered) }) {
        MDCH4("Library Search")
        MDCOverline("Search and filter kotlin multiplatform libraries")
      }
      Cell(span = 12u) {
        SearchForm()
      }
      Cell(span = 12u) {
        Cells {
          repeat(7) {
            Cell {
              SampleCard()
            }
          }
        }
      }
    }
  }
}

@Composable
fun SampleCard() {
  MDCCard {
    PrimaryAction {
      Media(
        type = MDCCardMediaType.Cinema,
        attrs = {
          style {
            backgroundImage("url('/images/kodex.svg')")
          }
        }
      ) {
        MediaContent { MDCH2("Title 1") }
      }
    }
    Actions {
      ActionButtons {
        ActionButton {
          Label("Action 1")
        }
        ActionButton {
          Label("Action 2")
        }
      }
      ActionIcons {
        ActionIconButton(attrs = { classes("material-icons") }) {
          Text("star")
        }
        ActionIconButton(attrs = { classes("material-icons") }) {
          Text("star")
        }
      }
    }
  }
}
