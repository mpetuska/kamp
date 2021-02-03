package app.component

import app.domain.*
import app.util.*
import kotlinx.html.*
import react.*
import react.dom.*

interface PaginatedContainerProps<T> : RProps {
  var fetchPage: suspend (Int) -> PagedResponse<T>
  var render: RBuilder.(List<T>) -> Unit
}

inline fun <T> RBuilder.PaginatedContainer(
  crossinline fetchPage: suspend (Int) -> PagedResponse<T>,
  render: RBuilder.(List<T>) -> Unit,
) {
  var page by useState(1)
  var pageData by useState<PagedResponse<T>?>(null)
  useEffect(listOf(page)) {
    suspending {
      pageData = fetchPage(page)
    }
  }
  div("container") {
    val data = pageData?.data
    if (data != null) {
      render(data)
    } else {
      div("text-center") {
        div("spinner-border text-light m-5") {
          attrs {
            role = "status"
          }
          span("sr-only") { +"Loading..." }
        }
      }
    }
  }
}
