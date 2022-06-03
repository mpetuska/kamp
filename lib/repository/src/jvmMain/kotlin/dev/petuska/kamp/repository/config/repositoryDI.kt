package dev.petuska.kamp.repository.config

import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.LibrariesStatistic
import dev.petuska.kamp.repository.LibraryRepository
import dev.petuska.kamp.repository.StatisticRepository
import kotlinx.coroutines.runBlocking
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo.createClient

fun repositoryDI(
  mongoString: String,
  mongoDatabase: String
) = DI.Module("repository") {
  bindSingleton { createClient(mongoString).coroutine }
  bindSingleton("libraries") {
    instance<CoroutineClient>()
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
    instance<CoroutineClient>()
      .getDatabase(mongoDatabase)
      .getCollection<LibrariesStatistic>("statistics")
  }
  bindSingleton { LibraryRepository(instance("libraries")) }
  bindSingleton { StatisticRepository(instance("statistics")) }
}
