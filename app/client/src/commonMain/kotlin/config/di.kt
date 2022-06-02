package dev.petuska.kamp.client.config

import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.state.AppState
import dev.petuska.kamp.client.util.UrlUtils
import dev.petuska.kamp.core.service.LibraryService
import dev.petuska.kamp.core.service.LibraryServiceImpl
import dev.petuska.kamp.core.util.DIModule
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.cbor.cbor
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import org.kodein.di.*
import org.reduxkotlin.Store

private fun loadServices(): DI.Module {
  val services by DIModule {
    bind<LibraryService>() with provider { LibraryServiceImpl(instance(), instance()) }
  }
  return services
}

fun loadDI(env: AppEnv, store: Store<AppState>) = DI {
  bind<Json>() with provider { Json }
  bind<Cbor>() with provider { Cbor }
  bind<HttpClient>() with singleton {
    HttpClient {
      install(ContentNegotiation) {
        json(instance())
        cbor(instance())
      }
      defaultRequest {
        accept(ContentType.Application.Cbor)
        contentType(ContentType.Application.Cbor)
      }
    }
  }
  bind<AppEnv>() with instance(env)
  bind<AppStore>() with instance(store)
  bind<UrlUtils>() with singleton { UrlUtils(instance()) }
  import(loadServices())
}
