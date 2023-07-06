package dev.petuska.kodex.repository.config

import dev.petuska.kodex.core.domain.KotlinLibrary
import dev.petuska.kodex.core.domain.LibrariesStatistic
import dev.petuska.kodex.repository.LibraryRepository
import dev.petuska.kodex.repository.StatisticRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.onClose
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo.createClient

fun repositoryModule(
  mongoString: String,
  mongoDatabase: String,
) = module {
  single { createClient(mongoString).coroutine } withOptions {
    onClose { it?.close() }
  }
  single(named("libraries")) {
    get<CoroutineClient>()
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
  single(named("statistics")) {
    get<CoroutineClient>()
      .getDatabase(mongoDatabase)
      .getCollection<LibrariesStatistic>("statistics")
  }

  single { LibraryRepository(get(named("libraries"))) }
  single { StatisticRepository(get(named("statistics"))) }
}
