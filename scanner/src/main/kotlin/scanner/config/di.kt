package scanner.config

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.auth.Auth
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
import scanner.domain.Repository
import scanner.processor.GradleModuleProcessor
import scanner.processor.PomProcessor
import scanner.service.MavenScannerService
import scanner.service.MavenScannerServiceImpl
import scanner.util.PrivateEnv
import scanner.util.prettyJson
import kotlin.time.minutes

fun HttpClientConfig<CIOEngineConfig>.baseConfig() {
  val timeout = 2.5.minutes.inMilliseconds.toLong()
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
            username = PrivateEnv.ADMIN_USER
            password = PrivateEnv.ADMIN_PASSWORD
          }
        }
      }
    }
  }
}
