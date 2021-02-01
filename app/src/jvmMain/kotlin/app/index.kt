package app

import app.service.*
import app.util.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import kamp.domain.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
  install(CallLogging)
  install(CORS) {
    host("*")
  }
  routing {
    route("/api") {
      get("/greet") {
        call.respond("Hi, ${call.request.queryParameters["name"] ?: "Anon!"}")
      }
      route("/library") {
        get {
          val res = LibraryService.getAll(
            call.request.queryParameters["page"]?.toIntOrNull() ?: 1,
            call.request.queryParameters["size"]?.toIntOrNull() ?: 50
          )
          val next = res.nextPage?.let { nextPage ->
            URLBuilder(call.request.uri).apply {
              parameters["page"] = "$nextPage"
              parameters["size"] = "${res.size}"
            }.buildString()
          }
          call.respond(PagedHttpResponse(res.data, next, res.total))
        }
      }
    }
    
    get("/application.env") {
      call.respondText("$Env")
    }
    
    static {
      val folder = "WEB-INF"
      val index = "$folder/index.html"
      files(folder)
      default(index)
      resources(folder)
      defaultResource(index)
    }
  }
  log.info("ENV: $Env")
  log.info("Full Env: ${System.getenv()}")
}
