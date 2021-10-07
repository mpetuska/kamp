package app.client.view.page.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.client.AppContext
import app.client.store.action.AppAction
import app.client.store.thunk.fetchLibraryPage
import app.client.util.select
import app.client.view.style.AppStyle
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.button.MDCButtonIcon
import dev.petuska.kmdc.button.MDCButtonLabel
import dev.petuska.kmdc.button.MDCButtonOpts
import dev.petuska.kmdc.checkbox.MDCCheckbox
import dev.petuska.kmdc.form.field.MDCFormField
import dev.petuska.kmdc.layout.grid.MDCLayoutGridCell
import dev.petuska.kmdc.layout.grid.MDCLayoutGridCells
import dev.petuska.kmdc.layout.grid.MDCLayoutGridScope
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.textfield.MDCTextFieldCommonOpts
import dev.petuska.kmdc.typography.MDCOverline
import domain.KotlinTarget
import org.jetbrains.compose.web.dom.Text

@Composable
fun AppContext.SearchForm(mdcLayoutGridScope: MDCLayoutGridScope) {
  with(mdcLayoutGridScope) {
    MDCLayoutGridCells {
      MDCLayoutGridCell({ span = 12u }) {
        TextFilter(this)
      }
      MDCLayoutGridCell({ span = 12u }) {
        TargetsFilter(this)
      }
    }
  }
}

@Composable
private fun AppContext.TargetsFilter(mdcLayoutGridScope: MDCLayoutGridScope) = with(mdcLayoutGridScope) {
  MDCLayoutGridCells {
    MDCLayoutGridCell({ span = 12u }, { classes(AppStyle.centered) }) {
      MDCOverline("Targets Filter")
    }
    MDCLayoutGridCell {
      TargetGroup("js", setOf("js:ir", "js:legacy"))
    }
    arrayOf(
      KotlinTarget.Common(),
      KotlinTarget.JVM.Java(),
      KotlinTarget.JS.IR(),
      KotlinTarget.Native("linuxX64")
    ).forEach {
      MDCLayoutGridCell {
        TargetGroup(it.category, setOf(it.toString()))
      }
    }
  }
}

@Composable
private fun AppContext.TargetGroup(
  category: String,
  groupTargets: Set<String>
) {
  val selectedTargets by select { targets ?: setOf() }
  val allSelected = remember(selectedTargets) {
    selectedTargets.containsAll(groupTargets)
  }
  val noneSelected = remember(selectedTargets) {
    groupTargets.none { it in selectedTargets }
  }
  MDCFormField {
    MDCCheckbox(
      opts = {
        label = category
        indeterminate = !allSelected && !noneSelected
      },
      attrs = {
        checked(allSelected)
        onInput {
          if (it.value) {
            groupTargets.forEach { target ->
              dispatch(AppAction.AddTarget(target))
            }
          } else {
            groupTargets.forEach { target ->
              dispatch(AppAction.RemoveTarget(target))
            }
          }
        }
      }
    )
  }
  groupTargets.forEach { target ->
    MDCFormField {
      MDCCheckbox(
        opts = {
          label = target
        },
        attrs = {
          checked(target in selectedTargets)
          onInput {
            println("[$target]. Selected: $selectedTargets, $allSelected, $noneSelected")
            if (it.value) {
              dispatch(AppAction.AddTarget(target))
            } else {
              dispatch(AppAction.RemoveTarget(target))
            }
          }
        }
      )
    }
  }
}

@Composable
private fun AppContext.TextFilter(mdcLayoutGridScope: MDCLayoutGridScope) = with(mdcLayoutGridScope) {
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
  MDCLayoutGridCells(attrs = { classes(AppStyle.centered) }) {
    MDCLayoutGridCell({ span = 6u }, { classes(AppStyle.centeredRight) }) {
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
    MDCLayoutGridCell({ span = 6u }, { classes(AppStyle.centeredLeft) }) {
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
