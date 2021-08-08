package app.client.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.util.FABIcon
import app.client.util.select
import app.client.view.component.FlexColumn
import app.client.view.component.FlexRow
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
import dev.petuska.kmdc.icon.button.MDCIconButton
import dev.petuska.kmdc.layout.grid.MDCLayoutGrid
import dev.petuska.kmdc.layout.grid.MDCLayoutGridCell
import dev.petuska.kmdc.layout.grid.MDCLayoutGridCells
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContext
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarContextOpts
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarMain
import dev.petuska.kmdc.typography.MDCH1
import dev.petuska.kmdc.typography.MDCTypography
import org.jetbrains.compose.web.css.backgroundImage
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun AppContext.App() {
  MDCTopAppBarContext({ type = MDCTopAppBarContextOpts.Type.Fixed }) {
    Navbar()
    MDCTopAppBarMain {
      MDCTypography()
      MDCLayoutGrid {
        MDCLayoutGridCells {
          MDCButtonOpts.Type.values().forEach {
            MDCLayoutGridCell {
              MDCButton({ type = it }) { Text(it.name) }
            }
          }
        }
      }

      FlexColumn {
        val count by select { count }
        Div(attrs = { style { padding(25.px) } }) {
          Button(
            attrs = {
              onClick { dispatch(AppAction.DecrementCount) }
            }
          ) {
            Text("-")
          }
          Span(attrs = { style { padding(15.px) } }) {
            Text("$count")
          }
          Button(
            attrs = {
              onClick { dispatch(AppAction.IncrementCount) }
            }
          ) {
            Text("+")
          }
        }
        FlexRow {
          SampleCard()
          SampleCard()
        }

        MDCLayoutGrid {
          MDCLayoutGridCells {
            repeat(50) {
              MDCLayoutGridCell {
                MDCIconButton {
                  FABIcon("android")
                }
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
            backgroundImage("url('http://www.wallpapers13.com/wp-content/uploads/2016/01/Peaceful-spring-sunset-2560x1600-wide-wallpapers-1920x1440.jpg')")
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
