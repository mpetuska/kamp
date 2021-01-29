package app

import dev.fritz2.binding.*
import dev.fritz2.dom.*
import dev.fritz2.dom.html.*
import dev.fritz2.remote.*
import kotlinx.browser.*

val store = storeOf("Hey there")
val client = http("https://kamp.azurewebsites.net/api").acceptJson()
  .contentType("application/json")

fun main() {
  window.onload = {
    render {
      div { store.data.asText() }
      button {
        clicks handledBy store.handle {
          client.get("/greet?name=Pedro").getBody()
        }
        +"Greet backend"
      }
    }.mount("root")
  }
}
