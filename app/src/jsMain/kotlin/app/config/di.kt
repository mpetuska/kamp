package app.config

import app.service.*
import app.util.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import org.kodein.di.*


private val services by DIModule {
  bind<LibraryService>() with provider { LibraryService(instance()) }
}

val di = DI {
  bind() from provider {
    Json {}
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
