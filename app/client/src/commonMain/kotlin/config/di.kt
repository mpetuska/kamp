package dev.petuska.kodex.client.config

import dev.petuska.kodex.client.service.LibraryService
import dev.petuska.kodex.client.store.AppStore
import dev.petuska.kodex.client.store.state.AppState
import dev.petuska.kodex.client.util.UrlUtils
import dev.petuska.kodex.core.config.serialisation
import dev.petuska.kodex.core.util.DIModule
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.cbor.cbor
import io.ktor.serialization.kotlinx.json.json
import org.kodein.di.*
import org.reduxkotlin.Store

fun loadDI(env: AppEnv, store: Store<AppState>) = DI {
  import(serialisation)
  import(services)
  bind<HttpClient>("kodex") with singleton {
    HttpClient {
      install(ContentNegotiation) {
        json(instance())
        cbor(instance())
      }
      defaultRequest {
        accept(ContentType.Application.Cbor)
        accept(ContentType.Application.Json)
        contentType(ContentType.Application.Cbor)
      }
    }
  }
  bind<AppEnv>() with instance(env)
  bind<AppStore>() with instance(store)
  bind<UrlUtils>() with singleton { UrlUtils(instance()) }
}

private val services by DIModule {
  bindSingleton { LibraryService(instance("kodex"), instance()) }
}
