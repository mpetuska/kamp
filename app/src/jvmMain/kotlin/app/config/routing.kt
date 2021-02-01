package app.config

import app.service.*
import app.util.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kamp.domain.*


fun Application.routing() = routing {
  api()
  get("/application.env") {
    call.respondText("$Env")
  }
  staticContent()
}

private fun Routing.api() = route("/api") {
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

private fun Routing.staticContent() = static {
  val folder = "WEB-INF"
  val index = "$folder/index.html"
  files(folder)
  default(index)
  resources(folder)
  defaultResource(index)
}
