package dev.petuska.kodex.cli.config

import dev.petuska.kodex.cli.cmd.KodexCmd
import dev.petuska.kodex.cli.cmd.capture.CaptureCmd
import dev.petuska.kodex.cli.cmd.scan.ScanCmd
import dev.petuska.kodex.cli.util.PrivateEnv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.cbor.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.module.dsl.onClose
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

val cmdModule = module {
  singleOf(::KodexCmd)
  singleOf(::ScanCmd)
  singleOf(::CaptureCmd)
}

val clientsModule = module {
  factory {
    HttpClient(CIO) {
      val timeout = 2.5.minutes.toLong(DurationUnit.MILLISECONDS)
      engine {
        requestTimeout = timeout
        maxConnectionsCount = 64
      }
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
  factory(named("kodex")) {
    get<HttpClient>().config {
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
  } withOptions {
    onClose { it?.close() }
  }
}
