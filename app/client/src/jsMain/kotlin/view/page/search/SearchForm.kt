package dev.petuska.kamp.client.view.page.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.action.AppAction
import dev.petuska.kamp.client.store.thunk.fetchLibraryPage
import dev.petuska.kamp.client.util.Routing
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
    val targetGroups = remember {
      mapOf(
        KotlinTarget.Common.category to setOf(KotlinTarget.Common),
        KotlinTarget.JVM.CATEGORY to KotlinTarget.JVM.values(),
        KotlinTarget.JS.CATEGORY to KotlinTarget.JS.values(),
      ) + KotlinTarget.Native.values().groupBy { it.family }.map { (k, v) -> k to v.toSet() }
    }
    targetGroups.forEach { (category, targets) ->
      MDCLayoutGridCell {
        TargetGroup(category, targets)
      }
    }
  }
}

@Composable
private fun TargetGroup(
  category: String,
  groupTargets: Set<KotlinTarget>
) {
  val store by rememberInstance<AppStore>()
  val selectedTargets by select { (targets ?: setOf()).filter { it in groupTargets } }
  val (allSelected, noneSelected) = remember(selectedTargets) {
    selectedTargets.containsAll(groupTargets) to groupTargets.none { it in selectedTargets }
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
            store.dispatch(AppAction.AddTargets(groupTargets))
          } else {
            store.dispatch(AppAction.RemoveTargets(groupTargets))
          }
        }
      }
    )
  }
  groupTargets.forEach { target ->
    MDCFormField {
      MDCCheckbox(
        opts = {
          label = target.toString()
        },
        attrs = {
          checked(target in selectedTargets)
          onInput {
            if (it.value) {
              store.dispatch(AppAction.AddTargets(target))
            } else {
              store.dispatch(AppAction.RemoveTargets(target))
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

  val submit: () -> Unit = remember(search, targets) {
    {
      store.dispatch(
        libraryService.fetchLibraryPage(
          page = 1,
          search = search,
          targets = targets,
        )
      )
      Routing.setQuery(mapOf("search" to search, "target" to targets?.map { it.platform }))
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
