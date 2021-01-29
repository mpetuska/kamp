package app

import dev.fritz2.binding.*
import dev.fritz2.dom.*
import dev.fritz2.dom.html.*
import dev.fritz2.remote.*
import kotlinx.browser.*
import kotlinx.coroutines.*

val store = storeOf("Hey there")
val client by lazy {
  http(window.env.API_URL).acceptJson()
    .contentType("application/json")
}

suspend fun main() {
  val env = loadEnv()
  val onLoad = suspend {
    env.await()
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
  window.onload = {
    GlobalScope.launch { onLoad() }
  }
}
