package scanner.config

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.kodein.di.*
import scanner.client.*
import scanner.domain.*
import scanner.domain.Repository.GRADLE_PLUGIN_PORTAL
import scanner.domain.Repository.MAVEN_CENTRAL
import scanner.processor.*
import scanner.service.*
import scanner.util.*
import kotlin.time.*


fun HttpClientConfig<CIOEngineConfig>.baseConfig() {
  val timeout = 2.5.minutes.inMilliseconds.toLong()
  engine {
    requestTimeout = timeout
  }
  defaultRequest {
    contentType(ContentType.Application.Json)
    accept(ContentType.Application.Json)
  }
  install(HttpTimeout) {
    requestTimeoutMillis = timeout
    connectTimeoutMillis = timeout
    socketTimeoutMillis = timeout
  }
  install(JsonFeature) {
    serializer = KotlinxSerializer(prettyJson)
  }
}

val di = DI {
  bind(MAVEN_CENTRAL.alias) from provider { MavenCentralClient(instance(), instance()) }
  bind(GRADLE_PLUGIN_PORTAL.alias) from provider { GradlePluginPortalClient(instance(), instance()) }
  bind<MavenScannerService<*>>(MAVEN_CENTRAL.alias) with singleton { MavenScannerServiceImpl(instance(MAVEN_CENTRAL.alias), instance(), instance()) }
  bind<MavenScannerService<*>>(GRADLE_PLUGIN_PORTAL.alias) with singleton { MavenScannerServiceImpl(instance(GRADLE_PLUGIN_PORTAL.alias), instance(), instance()) }
  
  bind() from singleton {
    kotlinx.serialization.json.Json {
      prettyPrint = true
      ignoreUnknownKeys = true
    }
  }
  bind() from singleton { PomProcessor() }
  bind() from singleton { GradleModuleProcessor() }
  
  bind() from provider { HttpClient(CIO, HttpClientConfig<CIOEngineConfig>::baseConfig) }
  bind("kamp") from provider {
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
