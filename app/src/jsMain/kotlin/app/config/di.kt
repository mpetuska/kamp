package app.config

import app.service.LibraryService
import app.util.DIModule
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

private val services by DIModule {
  bind<LibraryService>() with provider { LibraryService(instance()) }
}

val di = DI {
  bind {
    provider {
      Json {}
    }
  }
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
  import(services)
}
