package dev.petuska.kodex.server.config

import dev.petuska.kodex.repository.LibraryRepository
import dev.petuska.kodex.repository.StatisticRepository
import dev.petuska.kodex.server.util.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.routing() = routing {
  api()
  staticContent()
}

private fun Route.api() = route("/api") {
  get("/status") { call.respond(HttpStatusCode.OK) }
  libraries()
  statistics()
}

private fun Route.libraries() = route("/libraries") {
  val repo by inject<LibraryRepository>()
  get {
    call.respond(
      repo.search(
        page = call.request.page,
        size = call.request.pageSize,
        search = call.request.search,
        targets = call.request.targets
      ).let(call.request::paginateResponse)
    )
  }
  get("/count") {
    call.respond(
      repo.count(
        search = call.request.search,
        targets = call.request.targets
      )
    )
  }
}

private fun Route.statistics() = route("/statistics") {
  val repo by inject<StatisticRepository>()
  get {
    call.respond(
      repo.search(
        page = call.request.page,
        size = call.request.pageSize,
        from = call.request.from,
        to = call.request.to
      ).let(call.request::paginateResponse)
    )
  }
  get("/count") {
    call.respond(
      repo.count(
        from = call.request.from,
        to = call.request.to
      )
    )
  }
}

private fun Routing.staticContent() {
  get("/application.env") { call.respondText("$PublicEnv") }
  val folder = "WEB-INF"
  staticFiles("/", File(folder))
  staticResources("/", folder)
}
