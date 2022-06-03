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
import dev.petuska.kmdc.button.Icon
import dev.petuska.kmdc.button.Label
import dev.petuska.kmdc.button.MDCButton
import dev.petuska.kmdc.button.MDCButtonType
import dev.petuska.kmdc.checkbox.MDCCheckbox
import dev.petuska.kmdc.form.field.MDCFormField
import dev.petuska.kmdc.layout.grid.Cell
import dev.petuska.kmdc.layout.grid.Cells
import dev.petuska.kmdc.layout.grid.MDCLayoutGridScope
import dev.petuska.kmdc.textfield.MDCTextField
import dev.petuska.kmdc.textfield.MDCTextFieldType
import dev.petuska.kmdc.typography.MDCOverline
import org.jetbrains.compose.web.dom.Text
import org.kodein.di.compose.rememberInstance

@Composable
fun MDCLayoutGridScope.SearchForm() {
  Cells {
    Cell(span = 12u) {
      TextFilter()
    }
    Cell(span = 12u) {
      TargetsFilter()
    }
  }
}

@Composable
private fun MDCLayoutGridScope.TargetsFilter() {
  Cells {
    Cell(span = 12u, attrs = { classes(AppStyle.centered) }) {
      MDCOverline("Targets Filter")
    }
    val targetGroups = remember {
      mapOf(
        KotlinTarget.Common.category to setOf(KotlinTarget.Common),
        KotlinTarget.JVM.category to KotlinTarget.JVM.values(),
        KotlinTarget.JS.category to KotlinTarget.JS.values(),
      ) + KotlinTarget.Native.values().groupBy { it.family }.map { (k, v) -> k to v.toSet() }
    }
    targetGroups.forEach { (category, targets) ->
      Cell {
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
      checked = allSelected,
      label = category,
      indeterminate = !allSelected && !noneSelected,
      attrs = {
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
        checked = target in selectedTargets,
        label = target.toString(),
        attrs = {
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
private fun MDCLayoutGridScope.TextFilter() {
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
  Cells(attrs = { classes(AppStyle.centered) }) {
    Cell(span = 6u, attrs = { classes(AppStyle.centeredRight) }) {
      MDCTextField(
        value = search ?: "",
        label = "Search by text",
        type = MDCTextFieldType.Outlined,
        attrs = {
          onKeyDown {
            if (it.key == "Enter") submit()
          }
          onInput { store.dispatch(AppAction.SetSearch(it.value.takeIf(String::isNotBlank))) }
        }
      )
    }
    Cell(span = 6u, attrs = { classes(AppStyle.centeredLeft) }) {
      MDCButton(
        type = MDCButtonType.Raised,
        attrs = {
          onClick { submit() }
        }
      ) {
        Icon(attrs = { classes("material-icons") }) { Text("search") }
        Label("Search")
      }
    }
  }
}
