package dev.petuska.kamp.client.config

import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.state.AppState
import dev.petuska.kamp.client.util.CborFeature
import dev.petuska.kamp.client.util.UrlUtils
import dev.petuska.kamp.core.service.LibraryService
import dev.petuska.kamp.core.service.LibraryServiceImpl
import dev.petuska.kamp.core.util.DIModule
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import org.reduxkotlin.Store

private fun loadServices(): DI.Module {
  val services by DIModule {
    bind<LibraryService>() with provider { LibraryServiceImpl(instance(), instance()) }
  }
  return services
}

fun loadDI(env: AppEnv, store: Store<AppState>) = DI {
  bind<Json>() with provider { Json }
  bind<HttpClient>() with singleton {
    HttpClient {
      install(JsonFeature) {
        serializer = KotlinxSerializer(instance())
      }
      install(CborFeature)
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
