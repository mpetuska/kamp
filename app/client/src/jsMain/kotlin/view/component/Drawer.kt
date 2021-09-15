package app.client.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.store.thunk.fetchLibraryPage
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
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.textfield.MDCTextFieldOpts
import dev.petuska.kmdc.typography.MDCOverline
import dev.petuska.kmdc.typography.mdcTypography
import org.jetbrains.compose.web.dom.Text

@Composable
fun AppContext.Drawer() {
  val open by select { drawerOpen }
  val search by select { search }
  val targets by select { targets }

  val submit = remember {
    {
      dispatch(AppAction.SetDrawer(false))
      dispatch(
        fetchLibraryPage(
          page = 1,
          search = search,
          targets = targets,
        )
      )
    }
  }
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
      MDCDrawerTitle("Library Search")
      MDCDrawerSubtitle("Find and filter libraries by supported targets")
    }
    MDCDrawerContent {
      MDCLayoutGrid {
        MDCLayoutGridCells {
          MDCLayoutGridCell({ span = 10u }) {
            MDCTextField(
              opts = {
                label = "Search by text"
                type = MDCTextFieldOpts.Type.Outlined
              },
              attrs = {
                onKeyDown {
                  if (it.key == "Enter") submit()
                }
                onInput { dispatch(AppAction.SetSearch(it.value.takeIf(String::isNotBlank))) }
                value(search ?: "")
              }
            )
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
                onClick { submit() }
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
