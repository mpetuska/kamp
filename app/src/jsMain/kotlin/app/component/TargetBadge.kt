package app.component

import kamp.domain.*
import react.*
import react.dom.*

private fun badgeColor(category: String) = when (category) {
  KotlinTarget.Common.category -> "info"
  KotlinTarget.JVM.category -> "danger"
  KotlinTarget.JS.category -> "warning"
  KotlinTarget.Native.category -> "primary"
  else -> "secondary"
}

fun RBuilder.TargetBadge(category: String, targets: Collection<KotlinTarget>) {
  if (targets.size > 1) {
    div("btn-group") {
      a(classes = "badge badge-pill badge-${badgeColor(category)} dropdown-toggle mr-2") {
        attrs {
          type = "button"
          set("data-toggle", "dropdown")
          set("aria-haspopup", "true")
          set("aria-expanded", "false")
        }
        +category
      }
      div("dropdown-menu") {
        for (target in targets) {
          a(classes = "dropdown-item") {
            +"${target.platform}${target.variant?.let { v -> " | $v" } ?: ""}"
          }
        }
      }
    }
  } else {
    span("badge badge-pill badge-${badgeColor(category)} mr-2") {
      +category
    }
  }
}
