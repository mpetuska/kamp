package app.client.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.util.FABIcon
import app.client.util.select
import app.client.view.component.FlexColumn
import app.client.view.component.Navbar
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.button.MDCButtonIcon
import dev.petuska.kmdc.button.MDCButtonLabel
import dev.petuska.kmdc.button.MDCButtonOpts
import dev.petuska.kmdc.button.MDCButtonRipple
import dev.petuska.kmdc.card.MDCCard
import dev.petuska.kmdc.card.MDCCardActionButton
import dev.petuska.kmdc.card.MDCCardActionButtons
import dev.petuska.kmdc.card.MDCCardActionIconButton
import dev.petuska.kmdc.card.MDCCardActionIconLink
import dev.petuska.kmdc.card.MDCCardActionIcons
import dev.petuska.kmdc.card.MDCCardActions
import dev.petuska.kmdc.card.MDCCardOpts
import dev.petuska.kmdc.icon.button.MDCIconButton
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarMain
import dev.petuska.kmdc.top.app.bar.MDCTopAppBarOpts
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun AppContext.App() {
  val navbarType = MDCTopAppBarOpts.Type.Fixed
  Navbar(navbarType)
  MDCTopAppBarMain(navbarType) {
    FlexColumn {
      val count by select { count }
      MDCButton(
        opts = {
          type = MDCButtonOpts.Type.Contained
          icon = MDCButtonOpts.MDCButtonIconType.Trailing
        }
      ) {
        MDCButtonRipple()
        MDCButtonLabel("Contained")
        MDCButtonIcon {
          I(attrs = { classes("fas", "fa-star") })
        }
      }
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

      MDCCard {
        Text("Hi")
      }
      MDCCard({ this.type = MDCCardOpts.Type.Outlined }) {
      Text("Hi too")
    }
      SampleCard()

      repeat(50) {
        MDCIconButton {
          FABIcon("android")
        }
      }
    }
  }
}

@Composable
fun SampleCard() {
  MDCCard {
    // MDCCardPrimaryAction {
    //   MDCCardMedia({ type = MDCCardMediaOpts.Type.Square }) {
    //     MDCCardMediaContent { Text("Title") }
    //   }
    // }
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
