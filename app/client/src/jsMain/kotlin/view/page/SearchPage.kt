package app.client.view.page

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
import dev.petuska.kmdc.list.MDCListGroup
import dev.petuska.kmdc.list.MDCListGroupSubheader
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.textfield.MDCTextFieldCommonOpts
import dev.petuska.kmdc.typography.MDCH1
import dev.petuska.kmdc.typography.MDCOverline
import dev.petuska.kmdc.typography.MDCSubtitle1
import domain.KotlinTarget
import org.jetbrains.compose.web.css.backgroundImage
import org.jetbrains.compose.web.dom.Text

@Composable
fun AppContext.SearchPage() {
  MDCH1("Library Search")
  MDCSubtitle1("Search and filter kotlin multiplatform libraries")
  SearchForm()
  MDCLayoutGrid {
    MDCLayoutGridCells {
      repeat(12) {
        MDCLayoutGridCell {
          SampleCard()
        }
      }
    }
  }
}

@Composable
private fun AppContext.SearchForm() {
  val search by select { search }
  val targets by select { targets }

  val submit = remember(search, targets) {
    {
      dispatch(
        fetchLibraryPage(
          page = 1,
          search = search,
          targets = targets,
        )
      )
    }
  }

  MDCLayoutGrid {
    MDCLayoutGridCells {
      MDCLayoutGridCell({ span = 12u }) {
        MDCLayoutGrid {
          MDCLayoutGridCells {
            MDCLayoutGridCell({ span = 12u }) {
              MDCOverline("Targets Filter")
            }
            arrayOf(
              KotlinTarget.Common(),
              KotlinTarget.JVM.Java(),
              KotlinTarget.JS.IR(),
              KotlinTarget.Native("linuxX64")
            ).forEach {
              MDCLayoutGridCell {
                MDCListGroup {
                  MDCListGroupSubheader(it.category)
                  MDCListGroupSubheader(it.category)
                  MDCListGroupSubheader(it.category)
                  // KotlinTargetGroup(
                  //   KotlinTarget.JS.category,
                  //   listOf("js:ir", "js:legacy"),
                  // )
                }
              }
            }
          }
        }
      }
      MDCLayoutGridCell({ span = 12u }) {
        MDCLayoutGrid {
          MDCLayoutGridCells {
            MDCLayoutGridCell {
              MDCTextField(
                opts = {
                  label = "Search by text"
                  type = MDCTextFieldCommonOpts.Type.Outlined
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
            MDCLayoutGridCell {
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
