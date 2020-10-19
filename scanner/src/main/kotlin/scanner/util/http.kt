package scanner.util

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.statement.*
import io.ktor.http.*
import scanner.*

suspend inline fun <R> httpClientWithNotFound(action: HttpClient.() -> R) = httpClientWithNotFound(null, action)
suspend inline fun <R> httpClientWithNotFound(default: R, action: HttpClient.() -> R) = HttpClient {
  install(JsonFeature) {
    serializer = KotlinxSerializer(prettyJson)
  }
  install(HttpTimeout) {
    val timeout = 5 * 60 * 1000L
    requestTimeoutMillis = timeout
    connectTimeoutMillis = timeout
    socketTimeoutMillis = timeout
  }
}.use {
  it.withNotFound {
    action()
  } ?: default
}

suspend inline fun <R> HttpClient.withNotFound(default: R, block: HttpClient.() -> R, retryCount: Int = 1): R {
  val maxRetries = retryCount.coerceAtLeast(0)
  var retries = 0
  var result: R = default
  do {
    try {
      result = block()
      break
    } catch (e: ClientRequestException) {
      if (e.response.status == HttpStatusCode.NotFound
        || e.response.readText().contains("You've reached a dark spot...")
      ) {
        break
      } else if (retries < maxRetries) {
        systemLogger.warn { "Error executing HTTP request, retrying [$retries/$retryCount]: ${e.message}" }
      } else {
        throw e
      }
    }
  } while (retries++ < maxRetries)
  return result
}

suspend inline fun <R> HttpClient.withNotFound(block: HttpClient.() -> R?) = withNotFound(null, block)
