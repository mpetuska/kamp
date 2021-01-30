package app

import dev.fritz2.binding.*
import dev.fritz2.dom.*
import dev.fritz2.dom.html.*
import dev.fritz2.remote.*
import kotlinx.browser.*
import kotlinx.coroutines.*

val greeting = storeOf("Hey there")
val name = storeOf("")
val client by lazy {
  http(window.env.API_URL).acceptJson()
    .contentType("application/json")
}

suspend fun main() {
  val env = loadEnv()
  val onLoad = suspend {
    env.await()
    render {
      div { greeting.data.asText() }
      input {
        value(name.data)
        changes.values() handledBy name.update
      }
      button {
        clicks handledBy greeting.handle {
          client.get("/greet?name=${name.current.ifBlank { "Pedro" }}").getBody()
        }
        +"Greet me!"
      }
    }.mount("root")
  }
  window.onload = {
    GlobalScope.launch { onLoad() }
  }
}
