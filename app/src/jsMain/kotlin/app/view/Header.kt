package app.view


import app.store.*
import app.store.thunk.*
import app.util.*
import app.view.component.*
import dev.fritz2.binding.*
import dev.fritz2.components.*
import dev.fritz2.dom.*
import dev.fritz2.dom.html.*
import dev.fritz2.styling.params.*
import kotlinx.coroutines.flow.*

@KampComponent
private fun RenderContext.stackUpClose(style: BasicParams.() -> Unit = {}, children: RenderContext.() -> Unit) = stackUp(style) {
  spacing { none }
  items(children)
}

@KampComponent
private fun RenderContext.SearchModal() = modal({
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
  fun RenderContext.TargetCheckboxGroup(targets: kotlin.collections.Map<String, String>, header: RenderContext.() -> Unit) = stackUpClose {
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
    stackUp({
      margins {
        top { normal }
      }
    }) {
      items {
        box {
          h3 { +"Text Search" }
          inputField(store = searchStore) {
            type("search")
            placeholder("Search...")
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
              TargetCheckboxGroup(mapOf(
                "common" to "common",
              )) {
                h4 { +"Metadata" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "jvm" to "jvm",
                "android" to "android",
              )) {
                h4 { +"JVM" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "js" to "js",
              )) {
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
              TargetCheckboxGroup(mapOf(
                "linuxArm32Hfp" to "linux_arm32_hfp",
                "linuxArm64" to "linux_arm64",
                "linuxMips32" to "linux_mips32",
                "linuxMipsel32" to "linux_mipsel32",
                "linuxX64" to "linux_x64",
              )) {
                h6 { +"Linux" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "mingwX64" to "mingw_x64",
                "mingwX86" to "mingw_x86",
              )) {
                h6 { +"Windows" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "androidNativeX64" to "android_x64",
                "androidNativeX86" to "android_x86",
                "androidNativeArm32" to "android_arm32",
                "androidNativeArm64" to "android_arm64",
              )) {
                h6 { +"Android NDK" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "tvosArm64" to "tvos_arm64",
                "tvosX64" to "tvos_x64",
              )) {
                h6 { +"tvOS" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "iosArm32" to "ios_arm32",
                "iosArm64" to "ios_arm64",
                "iosX64" to "ios_x64",
              )) {
                h6 { +"iOS" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "watchosArm32" to "watchos_arm32",
                "watchosArm64" to "watchos_arm64",
                "watchosX86" to "watchos_x86",
                "watchosX64" to "watchos_x64",
              )) {
                h6 { +"watchOS" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "macosX64" to "macos_x64",
              )) {
                h6 { +"macOS" }
              }
            }
            stackUpClose {
              TargetCheckboxGroup(mapOf(
                "wasm32" to "wasm32",
              )) {
                h6 { +"WebAssembly" }
              }
            }
          }
        }
        clickButton {
          text("Search")
          icon { fromTheme { search } }
        }.map {}.onEach {
          fetchLibraryPage(
            page = 1,
            search = searchStore.current.takeIf(String::isNotEmpty),
            targets = targetsStore.current.takeIf(Set<String>::isNotEmpty)
          )()
          fetchLibraryCount(
            search = searchStore.current.takeIf(String::isNotEmpty),
            targets = targetsStore.current.takeIf(Set<String>::isNotEmpty)
          )()
        } handledBy close
      }
    }
  }
}

@KampComponent
fun RenderContext.Pagination() = lineUp {
  spacing { none }
  items {
    LibraryStore.data.mapLatest { it.libraries }.mapNotNull { it }.render { libs ->
      clickButton {
        size { small }
        icon { fromTheme { caretLeft } }
        disabled(libs.prev == null)
      } handledBy fetchLibraryPage(libs.page - 1)
      clickButton {
        size { small }
        variant { outline }
        text("${libs.page}")
      }
      clickButton {
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
            background { color { primary } }
            color { lightGray }
          }
          alignItems { end }
        }) {
          href("/")
          KampIcon {
            size { "3rem" }
            color { primary }
            display { inline }
            css("border-radius: 50%")
          }
  
          styled(::span)({
            margins { left { smaller } }
            verticalAlign { sub }
            fontSize(sm = { large }, md = { larger })
            fontWeight { lighter }
            display(sm = { none }, md = { flex })
          }) { +"KAMP" }
        }
        LibraryStore.data.map { it.count }.render { count ->
          Badge({ success }, {
            margins {
              left { tiny }
            }
          }) {
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
