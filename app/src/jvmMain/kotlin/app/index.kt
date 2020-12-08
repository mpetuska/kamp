package app

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
  routing {
    get("/hello") {
      call.respond("Hi")
    }
    
    static {
      resources("WEB-INF")
      defaultResource("index.html")
    }
  }
}
