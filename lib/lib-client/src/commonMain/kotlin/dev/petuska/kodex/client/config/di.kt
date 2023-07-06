package dev.petuska.kodex.client.config

import dev.petuska.kodex.client.service.LibraryService
import dev.petuska.kodex.client.store.state.AppState
import dev.petuska.kodex.client.util.UrlUtils
import dev.petuska.kodex.core.config.serialisationModule
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.cbor.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.onClose
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.reduxkotlin.Store

fun startDI(env: AppEnv, store: Store<AppState>) = startKoin {
  modules(
    serialisationModule,
    services,
    clientsModule,
    module {
      singleOf(::UrlUtils)
      single { env }
      single { store }
    }
  )
}

private val services = module {
  single { LibraryService(get(), get()) }
}

private val clientsModule = module {
  factory {
    HttpClient {
      defaultRequest {
        contentType(ContentType.Application.Cbor)
        accept(ContentType.Application.Json)
        accept(ContentType.Application.Cbor)
        accept(ContentType.Text.Html)
      }
      install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 3)
        retryOnException(maxRetries = 3)
        exponentialDelay()
      }
      install(ContentNegotiation) {
        json(get(named("pretty")))
        @OptIn(ExperimentalSerializationApi::class)
        cbor(get())
      }
    }
  } withOptions {
    onClose { it?.close() }
  }
}
