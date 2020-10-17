package scanner.util

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

inline fun <R> httpClientWithNotFound(action: HttpClient.() -> R) = httpClientWithNotFound(null, action)
inline fun <R> httpClientWithNotFound(default: R, action: HttpClient.() -> R) = HttpClient {
  install(JsonFeature) {
    serializer = KotlinxSerializer()
  }
  install(HttpTimeout) {
    val timeout = 2 * 60 * 1000L
    requestTimeoutMillis = timeout
    connectTimeoutMillis = timeout
    socketTimeoutMillis = timeout
  }
}.use {
  it.withNotFound {
    action()
  } ?: default
}

inline fun <R> HttpClient.withNotFound(default: R, block: HttpClient.() -> R) = try {
  block()
} catch (e: ClientRequestException) {
  if (e.response.status == HttpStatusCode.NotFound) null else throw e
} ?: default

inline fun <R> HttpClient.withNotFound(block: HttpClient.() -> R?) = withNotFound(null, block)
