package scanner.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.*
import scanner.*

val httpClient by lazy {
  val timeout = 5 * 60 * 1000L
  HttpClient(CIO) {
    engine {
      requestTimeout = timeout
    }
    install(JsonFeature) {
      serializer = KotlinxSerializer(prettyJson)
    }
    install(HttpTimeout) {
      requestTimeoutMillis = timeout
      connectTimeoutMillis = timeout
      socketTimeoutMillis = timeout
    }
  }
}

suspend fun <R> HttpClient.withNotFound(default: R, block: suspend HttpClient.() -> R, retryCount: Int = 1): R {
  val maxRetries = retryCount.coerceAtLeast(0)
  var retries = 0
  var result: R = default
  do {
    try {
      result = block()
      break
    } catch (e: ResponseException) {
      val resTxt = e.response.readText()
      if (e.response.status in setOf(HttpStatusCode.NotFound, HttpStatusCode.BadGateway)
        || resTxt.contains("You've reached a dark spot...")
      ) {
        break
      } else if (retries < maxRetries) {
        systemLogger.warn { "Error executing HTTP request, retrying [$retries/$retryCount]: ${e.message}" }
      } else {
        systemLogger.error { "Http error ${e.response.status} ${e.response.request.url} \n$resTxt" }
        throw e
      }
    }
  } while (retries++ < maxRetries)
  return result
}

suspend fun <R> HttpClient.withNotFound(block: suspend HttpClient.() -> R?) = withNotFound(null, block)

suspend fun request(url: String, config: HttpRequestBuilder.() -> Unit = {}) = httpClient.withNotFound {
  get<HttpResponse>(url, config)
}

suspend inline fun <reified R> fetch(url: String, noinline config: HttpRequestBuilder.() -> Unit = {}) =
  fetch<R?>(url, null, config)

suspend inline fun <reified R> fetch(url: String, default: R, noinline config: HttpRequestBuilder.() -> Unit = {}): R =
  request(url, config)?.let {
    if (it.status in setOf(HttpStatusCode.NotFound, HttpStatusCode.BadGateway)) {
      null
    } else {
      val txt = it.readText()
      when {
        txt.contains("You've reached a dark spot...") -> {
          null
        }
        R::class == String::class -> txt as R
        else -> {
          try {
            prettyJson.decodeFromString<R>(txt)
          } catch (e: Exception) {
            systemLogger.error { "Error [$e] parsing ${R::class.qualifiedName} from [$txt]" }
            null
          }
        }
      }
    }
  } ?: default
