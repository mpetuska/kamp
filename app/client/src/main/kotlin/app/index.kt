package app

import dev.fritz2.binding.*
import dev.fritz2.dom.*
import dev.fritz2.dom.html.*
import dev.fritz2.remote.*
import kotlinx.browser.*

val store = storeOf("Hey there")
val client = http(window.origin).acceptJson()
  .contentType("application/json")

fun main() {
  window.onload = {
    render {
      div { store.data.asText() }
      button {
        clicks handledBy store.handle {
          client.get("/hello").getBody()
        }
        +"Greet backend"
      }
    }.mount("root")
  }
}
