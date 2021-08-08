package app.client.view

import androidx.compose.runtime.Composable
import app.client.AppContext
import app.client.view.component.Drawer
import app.client.view.component.Navbar
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.button.MDCButtonLabel
import dev.petuska.kmdc.button.MDCButtonOpts
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
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContext
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContextOpts
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarMain
import dev.petuska.kmdc.typography.MDCH1
import dev.petuska.kmdc.typography.mdcTypography
import org.jetbrains.compose.web.css.backgroundImage
import org.jetbrains.compose.web.dom.Text

@Composable
fun AppContext.App() {
  MDCTopAppBarContext({ type = MDCTopAppBarContextOpts.Type.Fixed }) {
    Drawer()
    Navbar(this)
    MDCTopAppBarMain(
      attrs = {
        mdcTypography()
      }
    ) {
      MDCLayoutGrid {
        MDCLayoutGridCells {
          MDCButtonOpts.Type.values().forEach {
            MDCLayoutGridCell {
              MDCButton({ type = it }) { Text(it.name) }
            }
          }
        }
      }
      MDCLayoutGrid {
        MDCLayoutGridCells {
          repeat(50) {
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
        MDCCardMediaContent { MDCH1("Title 1") }
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
