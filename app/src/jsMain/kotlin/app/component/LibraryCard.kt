package app.component

import app.util.*
import kamp.domain.*
import kotlinx.css.*
import kotlinx.html.*
import react.*
import react.dom.*
import styled.*

external interface Props : RProps {
  var lib: KotlinMPPLibrary
}

val LibraryCard by FC<Props> { props ->
  val lib = props.lib
  styledDiv {
    css {
      classes.addAll("card border-secondary mb-3 mw-25".split(" "))
      width = 30.rem
    }
    div(classes = "card-body") {
      div(classes = "card-title d-flex justify-content-between") {
        h4 { +lib.name }
        
        div("mb-2") {
          for ((category, targets) in lib.targets.groupBy(KotlinTarget::category)) {
            TargetBadge(category, targets)
          }
        }
      }
      h5(classes = "card-subtitle mb-2 text-muted") { +lib.group }
      div("dropdown-divider") {}
      lib.description?.let {
        p(classes = "card-text") {
          +it
        }
      }
      lib.website?.let {
        a(
          classes = "card-link",
          target = "_blank",
          href = it
        ) {
          +"Website"
        }
      }
      lib.scm?.let {
        a(
          classes = "card-link",
          target = "_blank",
          href = it
        ) {
          +"SCM"
        }
      }
    }
    div(classes = "card-footer") {
      ul(classes = "nav nav-tabs") {
        attrs {
          role = "tablist"
        }
        li(classes = "nav-item border border-bottom-0 border-dark rounded-top") {
          a(classes = "nav-link active", href = "#gradle") {
            attrs {
              id = "gradle-tab"
              role = "tab"
              set("data-toggle", "tab")
              set("aria-controls", "gradle")
              set("aria-selected", "true")
            }
            +"Gradle"
          }
        }
        li(classes = "nav-item border border-bottom-0 border-dark rounded-top") {
          a(classes = "nav-link", href = "#maven") {
            attrs {
              id = "maven-tab"
              role = "tab"
              set("data-toggle", "tab")
              set("aria-controls", "maven")
              set("aria-selected", "false")
            }
            +"Maven"
          }
        }
      }
      div(classes = "tab-content p-2 bg-dark border border-dark rounded-bottom rounded-right") {
        div(classes = "tab-pane fade show active") {
          attrs {
            id = "gradle"
            role = "tabpanel"
            set("aria-labelledby", "gradle-tab")
          }
          pre("m-0") {
            +"""implementation("${lib.path}")"""
          }
        }
        div(classes = "tab-pane fade bg-dark") {
          attrs {
            id = "maven"
            role = "tabpanel"
            set("aria-labelledby", "maven-tab")
          }
          pre("m-0") {
            +"""
              <dependency>
                <groupId>${lib.group}</groupId>
                <artifactId>${lib.name}</artifactId>
                <version>${lib.latestVersion}</version>
              </dependency>
            """.trimIndent()
          }
        }
      }
    }
  }
//  row("targets") {
//    for ((category, targets) in lib.targets.groupBy(KotlinTarget::category)) {
//      if (targets.size > 1) {
//        dropdown<String>(grouped = true) {
//          customToggle {
//            badge {
//              read(false)
//              +category
//            }
//          }
//          items {
//            for (target in targets) {
//              item("${target.platform}${target.variant?.let { v -> "|$v" } ?: ""}")
//            }
//          }
//        }
//      } else {
//        badge {
//          read(false)
//          +category
//        }
//      }
//    }
//  }
}
