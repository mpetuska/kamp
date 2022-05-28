package dev.petuska.kamp.server.config

import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.service.LibraryService
import dev.petuska.kamp.core.util.DIModule
import dev.petuska.kamp.server.service.LibraryServiceImpl
import dev.petuska.kamp.server.util.PrivateEnv
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking
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

fun Application.diConfig() = di { import(services) }

private val services by DIModule {
  bind<CoroutineClient>() with singleton { createClient(PrivateEnv.MONGO_STRING).coroutine }
  bind<CoroutineCollection<KotlinLibrary>>() with
    singleton {
      instance<CoroutineClient>()
        .getDatabase(PrivateEnv.MONGO_DATABASE)
        .getCollection<KotlinLibrary>("libraries")
        .also {
          runBlocking {
            it.createIndex(
              """{
                  group: "text",
                  name: "text",
                  description: "text"
                }
              """.trimIndent()
            )
          }
        }
    }
  bind<LibraryService>() with
    scoped(CallScope).singleton { LibraryServiceImpl(context, instance()) }
}
