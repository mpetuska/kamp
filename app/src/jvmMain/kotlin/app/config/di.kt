package app.config

import app.service.LibraryService
import app.util.DIModule
import app.util.PrivateEnv
import io.ktor.application.Application
import kamp.domain.KotlinMPPLibrary
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.CallScope
import org.kodein.di.ktor.di
import org.kodein.di.scoped
import org.kodein.di.singleton
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun Application.diConfig() = di {
  import(services)
}

private val services by DIModule {
  bind<CoroutineClient>() with singleton { KMongo.createClient(PrivateEnv.MONGO_STRING).coroutine }
  bind<CoroutineCollection<KotlinMPPLibrary>>() with singleton {
    instance<CoroutineClient>().getDatabase(PrivateEnv.MONGO_DATABASE).getCollection("libraries")
  }
  bind<LibraryService>() with scoped(CallScope).singleton { LibraryService(context, instance()) }
}
