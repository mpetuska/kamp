package dev.petuska.kamp.server.config

import dev.petuska.kamp.core.service.LibraryService
import dev.petuska.kamp.server.util.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.http.content.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.routing() = routing {
  libraries()
  get("/api/status") { call.respond(HttpStatusCode.OK) }
  get("/application.env") { call.respondText("$PublicEnv") }
  staticContent()
}

private fun Route.libraries() =
  route(LibraryService.PATH) {
    get {
      val service by inject<LibraryService>()
      call.respond(
        service.getAll(
          call.request.page,
          call.request.pageSize,
          call.request.search,
          call.request.targets
        )
      )
    }
    get("/count") {
      val service by inject<LibraryService>()
      call.respond(service.getCount(call.request.search, call.request.targets))
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
