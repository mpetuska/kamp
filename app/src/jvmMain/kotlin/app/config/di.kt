package app.config

import app.service.*
import app.util.*
import io.ktor.application.*
import org.kodein.di.*
import org.kodein.di.ktor.*

fun Application.diConfig() = di {
  import(services)
}

private val services by DIModule {
  bind<LibraryService>() with scoped(CallScope).singleton { LibraryService(context) }
  bind<GreetService>() with scoped(CallScope).singleton { GreetService() }
}
