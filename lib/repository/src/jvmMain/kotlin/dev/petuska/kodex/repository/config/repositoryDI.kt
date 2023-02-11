package dev.petuska.kodex.repository.config

import dev.petuska.kodex.core.di.ApplicationDIScope
import dev.petuska.kodex.core.domain.KotlinLibrary
import dev.petuska.kodex.core.domain.LibrariesStatistic
import dev.petuska.kodex.repository.LibraryRepository
import dev.petuska.kodex.repository.StatisticRepository
import kotlinx.coroutines.runBlocking
import org.kodein.di.*
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeCloseable
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo.createClient

@JvmInline
private value class MongoClient(val client: CoroutineClient) : ScopeCloseable {
  override fun close() {
    client.close()
  }
}

fun repositoryDI(
  mongoString: String,
  mongoDatabase: String,
  scope: Scope<Any> = ApplicationDIScope,
) = DI.Module("repository") {
  bind {
    scoped(scope).singleton { MongoClient(createClient(mongoString).coroutine) }
  }
  bindSingleton("libraries") {
    instance<MongoClient>().client
      .getDatabase(mongoDatabase)
      .getCollection<KotlinLibrary>("libraries")
      .apply {
        runBlocking {
          createIndex(
            """
              {
                group: "text",
                name: "text",
                description: "text"
              }
            """.trimIndent()
          )
        }
      }
  }
  bindSingleton("statistics") {
    instance<MongoClient>().client
      .getDatabase(mongoDatabase)
      .getCollection<LibrariesStatistic>("statistics")
  }
  bindSingleton { LibraryRepository(instance("libraries")) }
  bindSingleton { StatisticRepository(instance("statistics")) }
}
