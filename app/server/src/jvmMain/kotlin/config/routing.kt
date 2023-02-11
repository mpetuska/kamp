package dev.petuska.kodex.server.config

import dev.petuska.kodex.repository.LibraryRepository
import dev.petuska.kodex.repository.StatisticRepository
import dev.petuska.kodex.server.util.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

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
  get {
    val repo by closestDI().instance<LibraryRepository>()
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
    val repo by closestDI().instance<LibraryRepository>()
    call.respond(
      repo.count(
        search = call.request.search,
        targets = call.request.targets
      )
    )
  }
}

private fun Route.statistics() = route("/statistics") {
  get {
    val repo by closestDI().instance<StatisticRepository>()
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
    val repo by closestDI().instance<StatisticRepository>()
    call.respond(
      repo.count(
        from = call.request.from,
        to = call.request.to
      )
    )
  }
}

private fun Routing.staticContent() = static {
  get("/application.env") { call.respondText("$PublicEnv") }
  val folder = "WEB-INF"
  val index = "$folder/index.html"
  files(folder)
  default(index)
  resources(folder)
  defaultResource(index)
}
