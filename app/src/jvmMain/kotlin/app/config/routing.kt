package app.config

import app.service.*
import app.util.*
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*


fun Application.routing() = routing {
  api()
  get("/application.env") {
    call.respondText("$Env")
  }
  staticContent()
}

private fun Routing.api() = route("/api") {
  route("/greet") {
    get {
      val service by inject<GreetService>()
      call.respond(service.greet(call.request.queryParameters["name"]))
    }
  }
  route("/library") {
    get {
      val service by inject<LibraryService>()
      call.respond(service.getAll(call.request.page, call.request.pageSize))
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
