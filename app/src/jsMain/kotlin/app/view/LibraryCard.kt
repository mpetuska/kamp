package app.view

import io.kvision.core.*
import io.kvision.html.*
import io.kvision.panel.*
import io.kvision.toolbar.*
import io.kvision.utils.*
import kamp.domain.*


private fun badgeColor(category: String) = when (category) {
  KotlinTarget.Common.category -> "info"
  KotlinTarget.JVM.category -> "danger"
  KotlinTarget.JS.category -> "warning"
  KotlinTarget.Native.category -> "primary"
  else -> "secondary"
}

private fun Container.TargetBadge(category: String, targets: List<KotlinTarget>) {
  if (targets.size > 1 || targets.firstOrNull()?.variant != null) {
    buttonGroup {
      link(category, classes = setOf("badge", "badge-pill", "badge-${badgeColor(category)}", "dropdown-toggle", "mr-2")) {
        setAttribute("type", "button")
        setAttribute("data-toggle", "dropdown")
        setAttribute("aria-haspopup", "true")
        setAttribute("aria-expanded", "false")
      }
      div(classes = setOf("dropdown-menu")) {
        for (target in targets) {
          link("${target.platform}${target.variant?.let { v -> " | $v" } ?: ""}", classes = setOf("dropdown-item"))
        }
      }
    }
  } else {
    span(classes = setOf("badge", "badge-pill", "badge-${badgeColor(category)}", "mr-2")) {
      +(targets.firstOrNull()?.platform ?: category)
    }
  }
}

private fun targetPriority(target: String) = when (target) {
  KotlinTarget.Common.category -> 1
  KotlinTarget.JVM.category -> 2
  KotlinTarget.JS.category -> 3
  KotlinTarget.Native.category -> 4
  else -> 0
}

fun Container.LibraryCard(library: KotlinMPPLibrary) = div(classes = setOf("card", "border-secondary", "mb-3")) {
  maxWidth = 35.rem
  div(classes = setOf("card-body")) {
    div(classes = setOf("card-title", "d-flex", "justify-content-between")) {
      h4(library.name)
      div(classes = setOf("mb-2")) {
        val groupedTargets = library.targets.groupBy(KotlinTarget::category).entries.sortedWith { (keyA), (keyB) ->
          targetPriority(keyA) - targetPriority(keyB)
        }
        for ((category, targets) in groupedTargets) {
          TargetBadge(category, targets)
        }
      }
    }
    h5(library.group, classes = setOf("card-subtitle", "mb-2", "text-muted"))
    div(classes = setOf("dropdown-divider"))
    library.description?.let {
      p(it, classes = setOf("card-text"))
    }
    library.website?.let {
      link(
        label = "Website",
        classes = setOf("card-link"),
        target = "_blank",
        url = it
      )
    }
    library.scm?.let {
      link(
        label = "SCM",
        classes = setOf("card-link"),
        target = "_blank",
        url = it
      )
    }
  }
  div(classes = setOf("card-footer")) {
    tabPanel(classes = setOf()) {
      tab("Gradle") {
        val text = "implementation(\"${library.group}:${library.name}:${library.version}\")"
        div(
          """
          |<pre>
          |$text
          |</pre>
          """.trimMargin(),
          classes = setOf("bg-dark", "p-2", "m-0", "border", "border-dark", "rounded-bottom", "rounded-right"),
          rich = true,
        )
      }
      tab("Maven") {
        val text = """|&lt;dependency&gt;
                      |  &lt;groupId&gt;${library.group}&lt;/groupId&gt;
                      |  &lt;artifactId&gt;${library.name}&lt;/artifactId&gt;
                      |  &lt;version&gt;${library.version}&lt;/version&gt;
                      |&lt;/dependency&gt;
                   """.trimMargin()
        div(
          """
          |<pre>
          |$text
          |</pre>
        """.trimMargin(),
          classes = setOf("bg-dark", "p-2", "m-0", "border", "border-dark", "rounded-bottom", "rounded-right"),
          rich = true,
        )
      }
    }
  }
}
