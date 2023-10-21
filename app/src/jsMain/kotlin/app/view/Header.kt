package app.view

import app.store.LibraryStore
import app.store.thunk.fetchLibraryCount
import app.store.thunk.fetchLibraryPage
import app.util.styled
import app.view.component.Badge
import app.view.component.KampIcon
import app.view.component.Link
import dev.fritz2.binding.storeOf
import dev.fritz2.components.box
import dev.fritz2.components.checkbox
import dev.fritz2.components.clickButton
import dev.fritz2.components.gridBox
import dev.fritz2.components.inputField
import dev.fritz2.components.lineUp
import dev.fritz2.components.modal
import dev.fritz2.components.navBar
import dev.fritz2.components.spinner
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.styling.params.BasicParams
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

@KampComponent
private fun RenderContext.stackUpClose(style: BasicParams.() -> Unit = {}, children: RenderContext.() -> Unit) =
  stackUp(style) {
    spacing { none }
    items(children)
  }

@KampComponent
private fun RenderContext.SearchModal() = modal({
  maxHeight { "92.5vh" }
  overflow { auto }
}) { close ->
  val searchStore = storeOf("")
  val targetsStore = storeOf(setOf<String>())

  @KampComponent
  fun RenderContext.TargetCheckbox(values: Set<String>, label: RenderContext.() -> Unit = {}) {
    checkbox {
      checked(targetsStore.data.map { it.containsAll(values) })
      events {
        changes.states() handledBy targetsStore.handle { targets, checked ->
          if (checked) {
            targets + values
          } else {
            targets - values
          }
        }
      }
      label(label)
    }
  }

  @KampComponent
  fun RenderContext.TargetCheckbox(values: Set<String>, label: String) = TargetCheckbox(values) {
    label {
      sub { +label }
    }
  }

  @KampComponent
  fun RenderContext.TargetCheckboxGroup(
    targets: kotlin.collections.Map<String, String>,
    header: RenderContext.() -> Unit,
  ) = stackUpClose {
    lineUp {
      spacing { none }
      items {
        TargetCheckbox(targets.values.toSet(), header)
      }
    }
    targets.forEach { (name, value) ->
      TargetCheckbox(setOf(value), name)
    }
  }

  size { large }
  content {
    gridBox({
      margin { auto }
      columns { "1fr" }
      maxWidth { "100%" }
      overflow { auto }
      gap { small }
      width { minContent }
      css("align-self: center")
    }) {
      box {
        h3 { +"Text Search" }
        inputField(store = searchStore) {
          type("search")
          placeholder("Search...")
          attr("autofocus", "autofocus")
        }
      }
      stackUpClose {
        h3 { +"Targets" }
        gridBox({
          columns(
            sm = { repeat(3) { "1fr" } },
          )
          gap { small }
          margins {
            bottom { small }
          }
        }) {
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "common" to "common",
              )
            ) {
              h4 { +"Metadata" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "jvm" to "jvm",
                "android" to "android",
              )
            ) {
              h4 { +"JVM" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "legacy" to "legacy",
                "ir" to "ir",
              )
            ) {
              h4 { +"JS" }
            }
          }
        }
        h4 { +"Native" }
        gridBox({
          columns(
            sm = { repeat(2) { "1fr" } },
            md = { repeat(3) { "1fr" } },
          )
          gap { small }
        }) {
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "linuxArm32Hfp" to "linux_arm32_hfp",
                "linuxArm64" to "linux_arm64",
                "linuxMips32" to "linux_mips32",
                "linuxMipsel32" to "linux_mipsel32",
                "linuxX64" to "linux_x64",
              )
            ) {
              h6 { +"Linux" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "mingwX64" to "mingw_x64",
                "mingwX86" to "mingw_x86",
              )
            ) {
              h6 { +"Windows" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "androidNativeX64" to "android_x64",
                "androidNativeX86" to "android_x86",
                "androidNativeArm32" to "android_arm32",
                "androidNativeArm64" to "android_arm64",
              )
            ) {
              h6 { +"Android NDK" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "tvosArm64" to "tvos_arm64",
                "tvosX64" to "tvos_x64",
              )
            ) {
              h6 { +"tvOS" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "iosArm32" to "ios_arm32",
                "iosArm64" to "ios_arm64",
                "iosX64" to "ios_x64",
              )
            ) {
              h6 { +"iOS" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "watchosArm32" to "watchos_arm32",
                "watchosArm64" to "watchos_arm64",
                "watchosX86" to "watchos_x86",
                "watchosX64" to "watchos_x64",
              )
            ) {
              h6 { +"watchOS" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "macosX64" to "macos_x64",
              )
            ) {
              h6 { +"macOS" }
            }
          }
          stackUpClose {
            TargetCheckboxGroup(
              mapOf(
                "wasm32" to "wasm32",
              )
            ) {
              h6 { +"WebAssembly" }
            }
          }
        }
      }
      clickButton({
        css("justify-self: center")
        width { "100%" }
      }) {
        text("Search")
        icon { fromTheme { search } }
      }.map {}.onEach {
        fetchLibraryPage(
          page = 1,
          search = searchStore.current,
          targets = targetsStore.current
        )()
        fetchLibraryCount(
          search = searchStore.current,
          targets = targetsStore.current
        )()
      } handledBy close
    }
  }
}

@KampComponent
fun RenderContext.Pagination() = lineUp {
  spacing { none }
  items {
    LibraryStore.data.mapLatest { it.libraries }.mapNotNull { it }.render { libs ->
      clickButton({
        css("border-top-right-radius: 0")
        css("border-bottom-right-radius: 0")
      }) {
        size { small }
        icon { fromTheme { caretLeft } }
        disabled(libs.prev == null)
      } handledBy fetchLibraryPage(libs.page - 1)
      clickButton({
        css("border-radius: 0")
      }) {
        size { small }
        variant { outline }
        text("${libs.page}")
      }
      clickButton({
        css("border-top-left-radius: 0")
        css("border-bottom-left-radius: 0")
      }) {
        size { small }
        icon { fromTheme { caretRight } }
        disabled(libs.next == null)
      } handledBy fetchLibraryPage(libs.page + 1)
    }
  }
}

@KampComponent
fun RenderContext.Header() {
  styled(::div)({
  }) {
    navBar({
      border { width { "0" } }
      boxShadow { flat }
    }) {
      brand {
        styled(::a)({
          after {
            textAlign { center }
            background { color { primary.base } }
            color { gray100 }
          }
          alignItems { end }
        }) {
          href("/")
          KampIcon {
            size { "3rem" }
            color { primary.base }
            display { inline }
            css("border-radius: 50%")
          }

          styled(::span)({
            margins { left { smaller } }
            verticalAlign { sub }
            fontSize(sm = { large }, md = { larger })
            fontWeight { lighter }
            display(sm = { none }, md = { initial })
          }) { +"KAMP" }
        }
        LibraryStore.data.map { it.count }.render { count ->
          Badge(
            { success },
            {
              margins {
                left { tiny }
              }
            }
          ) {
            if (count != null) {
              +"$count Lib${if (count > 1) "s" else ""}"
            } else {
              spinner {
                speed("1s")
              }
            }
          }
        }
      }

      actions {
        lineUp({
          display(sm = { none }, md = { flex })
        }) {
          items {
            Link("https://github.com/mpetuska/kamp", "_new") {
              +"GitHub"
            }
          }
        }
        lineUp({
          alignItems { center }
          margins {
            left { tiny }
          }
        }) {
          spacing { tiny }
          items {
            Pagination()
            val modal = SearchModal()
            clickButton({
              display(sm = { inlineBlock }, md = { none })
            }) {
              icon { fromTheme { search } }
            } handledBy modal
            clickButton({
              display(sm = { none }, md = { inlineBlock })
            }) {
              text("Search")
              icon { fromTheme { search } }
            } handledBy modal
          }
        }
      }
    }
  }
}
