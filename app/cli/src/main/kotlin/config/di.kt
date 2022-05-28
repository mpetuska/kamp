package dev.petuska.kamp.cli.config

import dev.petuska.kamp.cli.domain.Repository
import dev.petuska.kamp.cli.processor.GradleModuleProcessor
import dev.petuska.kamp.cli.processor.PomProcessor
import dev.petuska.kamp.cli.service.MavenScannerService
import dev.petuska.kamp.cli.service.MavenScannerServiceImpl
import dev.petuska.kamp.cli.util.PrivateEnv
import dev.petuska.kamp.cli.util.prettyJson
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.BasicAuthCredentials
import io.ktor.client.features.auth.providers.basic
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
import kotlin.time.Duration
import kotlin.time.DurationUnit

fun HttpClientConfig<CIOEngineConfig>.baseConfig() {
  val timeout = Duration.minutes(2.5).toLong(DurationUnit.MILLISECONDS)
  engine { requestTimeout = timeout }
  defaultRequest {
    contentType(ContentType.Application.Json)
    accept(ContentType.Application.Json)
    accept(ContentType.Text.Html)
  }
  install(HttpTimeout) {
    requestTimeoutMillis = timeout
    connectTimeoutMillis = timeout
    socketTimeoutMillis = timeout
  }
  install(JsonFeature) { serializer = KotlinxSerializer(prettyJson) }
}

val di = DI {
  Repository.values().forEach { repo ->
    bind(repo.alias) { provider { with(repo) { client(url) } } }
    bind<MavenScannerService<*>>(repo.alias) with
      singleton { MavenScannerServiceImpl(instance(repo.alias), instance(), instance()) }
  }

  bind {
    singleton {
      Json {
        prettyPrint = true
        ignoreUnknownKeys = true
      }
    }
  }
  bind { singleton { PomProcessor() } }
  bind { singleton { GradleModuleProcessor() } }
  bind { provider { HttpClient(CIO, HttpClientConfig<CIOEngineConfig>::baseConfig) } }
  bind("kamp") {
    provider {
      HttpClient(CIO) {
        baseConfig()
        install(Auth) {
          basic {
            credentials {
              BasicAuthCredentials(
                username = PrivateEnv.ADMIN_USER, password = PrivateEnv.ADMIN_PASSWORD
              )
            }
          }
        }
      }
    }
  }
}
