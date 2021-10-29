package dev.petuska.kamp.client.view.page.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.thunk.fetchLibraryPage
import dev.petuska.kamp.client.util.select
import dev.petuska.kamp.client.view.style.AppStyle
import dev.petuska.kamp.core.domain.KotlinTarget
import dev.petuska.kamp.core.service.LibraryService
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
import org.jetbrains.compose.web.dom.Text
import org.kodein.di.compose.rememberInstance

@Composable
fun SearchForm(mdcLayoutGridScope: MDCLayoutGridScope) {
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
private fun TargetsFilter(mdcLayoutGridScope: MDCLayoutGridScope) = with(mdcLayoutGridScope) {
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
private fun TargetGroup(
  category: String,
  groupTargets: Set<String>
) {
  val store by rememberInstance<AppStore>()
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
       store.       dispatch(AppAction.AddTarget(target))
            }
          } else {
            groupTargets.forEach { target ->
        store.      dispatch(AppAction.RemoveTarget(target))
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
            if (it.value) {
        store.      dispatch(AppAction.AddTarget(target))
            } else {
       store.       dispatch(AppAction.RemoveTarget(target))
            }
          }
        }
      )
    }
  }
}

@Composable
private fun TextFilter(mdcLayoutGridScope: MDCLayoutGridScope) = with(mdcLayoutGridScope) {
val store by rememberInstance<AppStore>()
  val search by select { search }
  val targets by select { targets }
  val libraryService by rememberInstance<LibraryService>()

  val submit = remember(search, targets) {
    {
    store.  dispatch(
      libraryService.fetchLibraryPage(
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
          onInput { store.dispatch(AppAction.SetSearch(it.value.takeIf(String::isNotBlank))) }
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
