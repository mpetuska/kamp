package dev.petuska.kamp.client.view.page.search

import androidx.compose.runtime.Composable
import dev.petuska.kamp.client.view.style.AppStyle
import dev.petuska.kmdc.button.MDCButtonLabel
import dev.petuska.kmdc.card.MDCCard
import dev.petuska.kmdc.card.MDCCardActionButton
import dev.petuska.kmdc.card.MDCCardActionButtons
import dev.petuska.kmdc.card.MDCCardActionIconButton
import dev.petuska.kmdc.card.MDCCardActionIconLink
import dev.petuska.kmdc.card.MDCCardActionIcons
import dev.petuska.kmdc.card.MDCCardActions
import dev.petuska.kmdc.card.MDCCardMedia
import dev.petuska.kmdc.card.MDCCardMediaContent
import dev.petuska.kmdc.card.MDCCardMediaOpts
import dev.petuska.kmdc.card.MDCCardPrimaryAction
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.layout.grid.MDCLayoutGridCell
import dev.petuska.kmdc.layout.grid.MDCLayoutGridCells
import dev.petuska.kmdc.typography.MDCH2
import dev.petuska.kmdc.typography.MDCH4
import dev.petuska.kmdc.typography.MDCOverline
import org.jetbrains.compose.web.css.backgroundImage
import org.jetbrains.compose.web.dom.Text

@Composable
fun SearchPage() {
  MDCLayoutGrid {
    MDCLayoutGridCells {
      MDCLayoutGridCell({ span = 12u }, { classes(AppStyle.centered) }) {
        MDCH4("Library Search")
        MDCOverline("Search and filter kotlin multiplatform libraries")
      }
      MDCLayoutGridCell({ span = 12u }) {
        SearchForm(this)
      }
      MDCLayoutGridCell({ span = 12u }) {
        MDCLayoutGridCells {
          repeat(7) {
            MDCLayoutGridCell {
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
    MDCCardPrimaryAction {
      MDCCardMedia(
        opts = { type = MDCCardMediaOpts.Type.Cinema },
        attrs = {
          style {
            backgroundImage("url('/images/kamp.svg')")
          }
        }
      ) {
        MDCCardMediaContent { MDCH2("Title 1") }
      }
    }
    MDCCardActions {
      MDCCardActionButtons {
        MDCCardActionButton {
          MDCButtonLabel("Action 1")
        }
        MDCCardActionButton {
          MDCButtonLabel("Action 2")
        }
      }
      MDCCardActionIcons {
        MDCCardActionIconButton(attrs = { classes("material-icons") }) {
          Text("star")
        }
        MDCCardActionIconLink(attrs = { classes("material-icons") }) {
          Text("star")
        }
      }
    }
  }
}
