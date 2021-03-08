package app.view.component

import app.util.*
import app.view.*
import dev.fritz2.binding.*
import dev.fritz2.components.*
import dev.fritz2.dom.html.*
import dev.fritz2.styling.theme.*
import kamp.domain.*
import kotlinx.coroutines.flow.*


private val String.badgeColor: Colors.() -> Property
  get() = when (this) {
    KotlinTarget.Common.category -> ({ "#47d7ff" })
    KotlinTarget.JVM.category -> ({ "#79bf2d" })
    KotlinTarget.JS.category -> ({ "#ffb100" })
    KotlinTarget.Native.category -> ({ "#6d6dff" })
    else -> ({ gray })
  }

private fun targetPriority(target: String) = when (target) {
  KotlinTarget.Common.category -> 1
  KotlinTarget.JVM.category -> 2
  KotlinTarget.JS.category -> 3
  KotlinTarget.Native.category -> 4
  else -> 0
}

@KampComponent
private fun RenderContext.TargetBadge(category: String, targets: List<KotlinTarget>) {
  @KampComponent
  fun RenderContext.RenderBadge(content: Span.() -> Unit) {
    Badge(category.badgeColor, {
      css("cursor: pointer")
      height { large }
      margins {
        left { tiny }
        top { tiny }
      }
    }, content)
  }
  
  if (targets.size > 1 || targets.firstOrNull()?.variant != null) {
    popover({
      width { minContent }
      css("border-radius: 0.5rem")
      paddings {
        vertical { tiny }
      }
      boxShadow { flat }
      minWidth { "5rem" }
      textAlign { center }
      background {
        color(category.badgeColor)
      }
    }) {
      placement { bottom }
      hasCloseButton(false)
      
      toggle {
        RenderBadge {
          +category
          icon { fromTheme { caretDown } }
        }
      }
      content {
        for (target in targets) {
          styled(::div)({
            fontWeight { "500" }
            color { base }
            textShadow { flat }
          }) {
            +if (target.category == KotlinTarget.JS.category) {
              target.variant ?: "UNKNOWN"
            } else {
              target.platform
            }
          }
        }
      }
    }
  } else {
    RenderBadge { +(targets.firstOrNull()?.platform ?: category) }
  }
}

@KampComponent
private fun RenderContext.CardHeader(library: KotlinMPPLibrary) {
  val groupedTargets = library.targets.groupBy(KotlinTarget::category).entries.sortedWith { (keyA), (keyB) ->
    targetPriority(keyA) - targetPriority(keyB)
  }
  
  box {
    box({
      display { flex }
      alignContent { center }
      justifyContent { spaceBetween }
    }) {
      flexBox({
        direction { column }
      }) {
        styled(::h4)({
          margins {
            top { tiny }
          }
        }) { +library.name }
        styled(::sub)({
          margin { "0" }
        }) { +library.group }
      }
      box({
        margins {
          left { small }
        }
        justifyContent { flexEnd }
        css("flex-wrap: wrap")
        display { inlineFlex }
      }) {
        for ((category, targets) in groupedTargets) {
          TargetBadge(category, targets)
        }
      }
    }
    lineUp({
      justifyContent { flexEnd }
    }) {
      spacing { none }
      items {
        library.website?.let {
          Link(it, "_new") {
            +"Website"
          }
        }
        library.scm?.let {
          Link(it, "_new") {
            +"SCM"
          }
        }
      }
    }
  }
}

@KampComponent
private fun RenderContext.CardBody(library: KotlinMPPLibrary, selectedVersion: RootStore<String>) = lineUp({
}) {
  items {
    box({
      maxWidth { "7rem" }
    }) {
      styled(::h5)({ }) {
        +"Versions"
      }
      box({
        css("direction: rtl")
        minHeight { "1rem" }
        maxHeight { "5rem" }
        paddings { horizontal { tiny } }
        overflow { auto }
        width { "100%" }
        minWidth { "4rem" }
      }) {
        flexBox({
          css("direction: ltr")
          direction { columnReverse }
        }) {
          val versions = library.versions ?: listOf(library.version)
          for (version in versions) {
            selectedVersion.data.map { it == version }.render { isSelected ->
              clickButton({
                width { "100%" }
                height { minContent }
                paddings { vertical { "0.15rem" } }
                children("span") {
                  css("text-overflow: ellipsis")
                  overflow { hidden }
                }
              }) {
                size { small }
                text(version)
                variant { if (isSelected) solid else outline }
              }.map { version } handledBy selectedVersion.update
            }
          }
        }
      }
    }
    styled(::p)({
      height { "100%" }
    }) {
      library.description?.let { +it }
    }
  }
}

private enum class PackageManager {
  GRADLE, MAVEN
}

@KampComponent
private fun RenderContext.CardFooter(library: KotlinMPPLibrary, selectedVersion: RootStore<String>) = stackUp({
  margins { top { small } }
}) {
  val selectedPackageManager = storeOf(PackageManager.GRADLE)
  
  spacing { none }
  items {
    lineUp {
      spacing { none }
      items {
        selectedPackageManager.data.render { packageManager ->
          for (manager in PackageManager.values()) {
            box {
              clickButton({
                css("border-bottom-right-radius: 0")
                css("border-bottom-left-radius: 0")
                css("border-bottom: none")
              }) {
                variant { if (manager == packageManager) solid else outline }
                text(manager.name)
              }.map { manager } handledBy selectedPackageManager.update
            }
          }
        }
      }
    }
    box({
      width { "100%" }
      border {
        color { primary }
        width { thin }
      }
      css("border-bottom-left-radius: 0.5rem")
      css("border-bottom-right-radius: 0.5rem")
      css("border-top-right-radius: 0.5rem")
    }) {
      for (manager in PackageManager.values()) {
        when (manager) {
          PackageManager.GRADLE -> styled(::pre)({
            overflowX { auto }
            width { inherit }
            padding { tiny }
          }) {
            attr("hidden", selectedPackageManager.data.map { it != manager })
            selectedVersion.data.render { version ->
              domNode.innerText = "implementation(\"${library.group}:${library.name}:${version}\")"
            }
          }
          PackageManager.MAVEN -> styled(::pre)({
            overflowX { auto }
            width { inherit }
            padding { tiny }
          }) {
            attr("hidden", selectedPackageManager.data.map { it != manager })
            selectedVersion.data.render { version ->
              domNode.innerText = """|<dependency>
              |  <groupId>${library.group}</groupId>
              |  <artifactId>${library.name}</artifactId>
              |  <version>${version}</version>
              |</dependency>
             """.trimMargin()
            }
          }
        }
      }
    }
  }
}

@KampComponent
fun RenderContext.LibraryCard(library: KotlinMPPLibrary) {
  val selectedVersionStore = storeOf(library.version)
  
  box({
    border {
      width { "1px" }
    }
    boxShadow { flat }
    css("border-radius: 0.5em")
    padding { small }
    maxWidth(
      sm = { "22.5rem" },
      lg = { "30rem" },
    )
  }) {
    CardHeader(library)
    styled(::hr)({
      borders {
        top {
          color { lightGray }
          style { solid }
          width { "0.1rem" }
        }
      }
      css("border-radius: 0.5rem")
      margins { vertical { tiny } }
    }) {}
    CardBody(library, selectedVersionStore)
    CardFooter(library, selectedVersionStore)
  }
}
