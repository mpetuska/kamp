package app.config

import app.service.*
import app.util.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Application.routing() = routing {
  libraries()
  get("/application.env") {
    call.respondText("$PublicEnv")
  }
  staticContent()
}

private fun Routing.libraries() = route(LibraryService.path) {
  get {
    val service by inject<LibraryService>()
    call.respond(service.getAll(call.request.page, call.request.pageSize, call.request.search))
  }
  get("/count") {
    val service by inject<LibraryService>()
    call.respond(service.getCount())
  }
  
  authenticate {
    post {
      val service by inject<LibraryService>()
      val entity = service.create(call.receive())
      call.respond(HttpStatusCode.Created, entity)
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
