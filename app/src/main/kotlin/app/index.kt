package app

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*


@Suppress("EXPERIMENTAL_API_USAGE")
fun main() {
  embeddedServer(CIO, port = System.getenv()["PORT"]?.toIntOrNull() ?: 8080) {
    install(CallLogging)
    routing {
      get("/hello") {
        call.respond("Hi")
      }
      get("/test") {
        val resource = this::class.java.classLoader.getResource("/WEB-INF/index.html")?.readText() ?: "null"
        call.respond(resource)
      }
      
      static {
        val folder = "WEB-INF"
        files(folder)
        resources(folder)
        val index = "$folder/index.html"
        default(index)
        defaultResource(index)
      }
    }
  }.start(true)
}
