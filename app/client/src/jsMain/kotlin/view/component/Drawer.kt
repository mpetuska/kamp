package app.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.util.select
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.button.MDCButtonIcon
import dev.petuska.kmdc.button.MDCButtonLabel
import dev.petuska.kmdc.button.MDCButtonOpts
import dev.petuska.kmdc.drawer.MDCDrawer
import dev.petuska.kmdc.drawer.MDCDrawerContent
import dev.petuska.kmdc.drawer.MDCDrawerHeader
import dev.petuska.kmdc.drawer.MDCDrawerOpts
import dev.petuska.kmdc.drawer.MDCDrawerScrim
import dev.petuska.kmdc.drawer.MDCDrawerSubtitle
import dev.petuska.kmdc.drawer.MDCDrawerTitle
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.layout.grid.MDCLayoutGridCell
import dev.petuska.kmdc.layout.grid.MDCLayoutGridCells
import dev.petuska.kmdc.typography.MDCOverline
import dev.petuska.kmdc.typography.mdcTypography
import org.jetbrains.compose.web.dom.Text

@Composable
fun AppContext.Drawer() {
  val open by select { drawerOpen }
  MDCDrawer(
    opts = {
      type = MDCDrawerOpts.Type.Modal
      isOpen = open
    },
    attrs = {
      mdcTypography()
      addEventListener("MDCDrawer:opened") {
        dispatch(AppAction.SetDrawer(true))
      }
      addEventListener("MDCDrawer:closed") {
        dispatch(AppAction.SetDrawer(false))
      }
    }
  ) {
    MDCDrawerHeader {
      MDCDrawerTitle { Text("Library Search") }
      MDCDrawerSubtitle { Text("Find and filter libraries by supported targets") }
    }
    MDCDrawerContent {
      MDCLayoutGrid {
        MDCLayoutGridCells {
          MDCLayoutGridCell({ span = 10u }) {
            MDCOverline("Search by text")
          }
          MDCLayoutGridCell({ span = 10u }) {
            MDCOverline("Filter by targets")
          }
          MDCLayoutGridCell({ span = 10u }) {
            MDCButton(
              opts = {
                type = MDCButtonOpts.Type.Raised
              },
              attrs = {
                onClick { dispatch(AppAction.SetDrawer(false)) }
              }
            ) {
              MDCButtonIcon(attrs = { classes("material-icons") }) { Text("search") }
              MDCButtonLabel("Search")
            }
          }
        }
      }
    }
  }
  MDCDrawerScrim()
}
