package app.server.config

import app.server.service.LibraryServiceImpl
import app.server.util.PrivateEnv
import domain.KotlinMPPLibrary
import io.ktor.application.Application
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.CallScope
import org.kodein.di.ktor.di
import org.kodein.di.scoped
import org.kodein.di.singleton
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo.createClient
import service.LibraryService
import util.DIModule

fun Application.diConfig() = di {
  import(services)
}

private val services by DIModule {
  bind<CoroutineClient>() with singleton { createClient(PrivateEnv.MONGO_STRING).coroutine }
  bind<CoroutineCollection<KotlinMPPLibrary>>() with singleton {
    instance<CoroutineClient>().getDatabase(PrivateEnv.MONGO_DATABASE).getCollection("libraries")
  }
  bind<LibraryService>() with scoped(CallScope).singleton { LibraryServiceImpl(context, instance()) }
}
