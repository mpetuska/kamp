package app.view.component

import app.util.*
import app.view.*
import dev.fritz2.components.*
import dev.fritz2.dom.html.*
import dev.fritz2.styling.theme.*
import kamp.domain.*


private val String.badgeColor: Colors.() -> Property
  get() = when (this) {
    KotlinTarget.Common.category -> ({ "#47d7ff" })
    KotlinTarget.JVM.category -> ({ "#79bf2d" })
    KotlinTarget.JS.category -> ({ "#ffb100" })
    KotlinTarget.Native.category -> ({ "#6d6dff" })
    else -> ({ gray })
  }

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

private fun targetPriority(target: String) = when (target) {
  KotlinTarget.Common.category -> 1
  KotlinTarget.JVM.category -> 2
  KotlinTarget.JS.category -> 3
  KotlinTarget.Native.category -> 4
  else -> 0
}

private fun RenderContext.CardHeader(name: String, group: String, libraryTargets: Set<KotlinTarget>) {
  val groupedTargets = libraryTargets.groupBy(KotlinTarget::category).entries.sortedWith { (keyA), (keyB) ->
    targetPriority(keyA) - targetPriority(keyB)
  }
  
  box {
    box({
      display { flex }
      alignContent { center }
      justifyContent { spaceBetween }
    }) {
      styled(::h4)({
        margins {
          top { tiny }
        }
      }) { +name }
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
    sub { +group }
  }
}

fun RenderContext.LibraryCard(library: KotlinMPPLibrary) {
  box({
    border {
      width { "1px" }
    }
    boxShadow { flat }
    css("border-radius: 0.5em")
    padding { small }
    maxWidth(
      sm = { "22.5rem" },
      md = { "35rem" },
    )
  }) {
    CardHeader(library.name, library.group, library.targets)
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
    p {
      library.description?.let { +it }
    }
  }
//  div(classes = setOf("card", "border-secondary", "mb-3")) {
//    maxWidth = 35.rem
//    div(classes = setOf("card-body")) {
//      div(classes = setOf("card-title", "d-flex", "justify-content-between")) {
//        h4(library.name)
//        div(classes = setOf("mb-2")) {
//          val groupedTargets = library.targets.groupBy(KotlinTarget::category).entries.sortedWith { (keyA), (keyB) ->
//            targetPriority(keyA) - targetPriority(keyB)
//          }
//          for ((category, targets) in groupedTargets) {
//            TargetBadge(category, targets)
//          }
//        }
//      }
//      h5(library.group, classes = setOf("card-subtitle", "mb-2", "text-muted"))
//      div(classes = setOf("dropdown-divider"))
//      library.description?.let {
//        p(it, classes = setOf("card-text"))
//      }
//      library.website?.let {
//        link(
//          label = "Website",
//          classes = setOf("card-link"),
//          target = "_blank",
//          url = it
//        )
//      }
//      library.scm?.let {
//        link(
//          label = "SCM",
//          classes = setOf("card-link"),
//          target = "_blank",
//          url = it
//        )
//      }
//    }
//    div(classes = setOf("card-footer")) {
//      tabPanel(classes = setOf()) {
//        tab("Gradle") {
//          val text = "implementation(\"${library.group}:${library.name}:${library.version}\")"
//          div(
//            """
//          |<pre>
//          |$text
//          |</pre>
//          """.trimMargin(),
//            classes = setOf("bg-dark", "p-2", "m-0", "border", "border-dark", "rounded-bottom", "rounded-right"),
//            rich = true,
//          )
//        }
//        tab("Maven") {
//          val text = """|&lt;dependency&gt;
//                      |  &lt;groupId&gt;${library.group}&lt;/groupId&gt;
//                      |  &lt;artifactId&gt;${library.name}&lt;/artifactId&gt;
//                      |  &lt;version&gt;${library.version}&lt;/version&gt;
//                      |&lt;/dependency&gt;
//                   """.trimMargin()
//          div(
//            """
//          |<pre>
//          |$text
//          |</pre>
//        """.trimMargin(),
//            classes = setOf("bg-dark", "p-2", "m-0", "border", "border-dark", "rounded-bottom", "rounded-right"),
//            rich = true,
//          )
//        }
//      }
//    }
//  }
}
