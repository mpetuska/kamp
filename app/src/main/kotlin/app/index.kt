package app

import app.util.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
  install(CallLogging)
  routing {
    route("/api") {
      get("/greet") {
        call.respond("Hi, ${call.request.queryParameters["name"] ?: "Anon"}")
      }
      get("/test") {
        val resource = this::class.java.classLoader.getResource("/WEB-INF/index.html")?.readText() ?: "null"
        call.respond(resource)
      }
    }
  
    get("/application.env") {
      call.respondText("$Env")
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
  log.info("ENV: $Env")
}
