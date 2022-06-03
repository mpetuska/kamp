package dev.petuska.kamp.cli.config

import dev.petuska.kamp.cli.util.PrivateEnv
import dev.petuska.kamp.core.config.serialisation
import dev.petuska.kamp.repository.config.repositoryDI
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.cbor.cbor
import io.ktor.serialization.kotlinx.json.json
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

fun sharedDI() = DI {
  import(serialisation)
  import(repositoryDI(PrivateEnv.MONGO_STRING, PrivateEnv.MONGO_DATABASE))
  bindProvider {
    HttpClient(CIO) {
      val timeout = 2.5.minutes.toLong(DurationUnit.MILLISECONDS)
      engine { requestTimeout = timeout }
      defaultRequest {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        accept(ContentType.Application.Cbor)
        accept(ContentType.Text.Html)
      }
      install(HttpTimeout) {
        requestTimeoutMillis = timeout
        connectTimeoutMillis = timeout
        socketTimeoutMillis = timeout
      }
      install(ContentNegotiation) {
        json(instance("pretty"))
        cbor(instance())
      }
    }
  }
  bindProvider("kamp") {
    instance<HttpClient>().config {
      defaultRequest {
        url(PrivateEnv.API_URL)
      }
      install(Auth) {
        basic {
          credentials {
            BasicAuthCredentials(
              username = PrivateEnv.ADMIN_USER,
              password = PrivateEnv.ADMIN_PASSWORD
            )
          }
        }
      }
    }
  }
}
