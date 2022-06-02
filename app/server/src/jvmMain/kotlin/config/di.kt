package dev.petuska.kamp.server.config

import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.service.LibraryService
import dev.petuska.kamp.core.util.DIModule
import dev.petuska.kamp.server.service.LibraryServiceImpl
import dev.petuska.kamp.server.util.PrivateEnv
import io.ktor.server.application.Application
import kotlinx.coroutines.runBlocking
import org.kodein.di.*
import org.kodein.di.ktor.CallScope
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo.createClient

fun Application.diConfig() = DI { import(services) }

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
