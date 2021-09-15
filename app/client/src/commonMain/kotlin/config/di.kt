package app.client.config

import app.client.util.UrlUtils
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import service.LibraryService
import service.LibraryServiceImpl
import util.DIModule

private fun loadServices(): DI.Module {
  val services by DIModule {
    bind<LibraryService>() with provider { LibraryServiceImpl(instance(), instance()) }
  }
  return services
}

fun loadDI(env: AppEnv) = DI {
  bind<Json>() with provider { Json }
  bind<HttpClient>() with singleton {
    HttpClient {
      install(JsonFeature) {
        serializer = KotlinxSerializer(instance())
      }
      defaultRequest {
        contentType(ContentType.Application.Json)
      }
    }
  }
  bind<AppEnv>() with instance(env)
  bind<UrlUtils>() with singleton { UrlUtils(instance()) }
  import(loadServices())
}
