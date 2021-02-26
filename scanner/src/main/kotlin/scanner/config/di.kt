package scanner.config

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import org.kodein.di.*
import scanner.client.*
import scanner.processor.*
import scanner.service.*
import scanner.util.*


fun HttpClientConfig<CIOEngineConfig>.baseConfig() {
  defaultRequest {
    contentType(ContentType.Application.Json)
  }
  install(JsonFeature) {
    serializer = KotlinxSerializer(prettyJson)
  }
}

val di = DI {
  bind() from singleton { MavenCentralClient(di) }
  bind<MavenScannerService<*>>("mavenCentral") with singleton { MavenCentralScannerService(instance(), instance(), instance("1.4.30")) }
  
  bind() from singleton { PomProcessor() }
  bind("1.4.30") from singleton { GradleModuleProcessor() }
  
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