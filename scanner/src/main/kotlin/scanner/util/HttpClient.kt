package scanner.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
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

suspend fun request(url: String, config: HttpRequestBuilder.() -> Unit = {}): HttpResponse? {
  val action = suspend { httpClient.get<HttpResponse>(url, config) }
  return runCatching {
    action()
  }.recoverCatching { e ->
    when (e) {
      is ResponseException -> {
        val txt = e.response.readText()
        when {
          e.response.status in setOf(HttpStatusCode.NotFound,
            HttpStatusCode.BadGateway) || txt.contains("You've reached a dark spot...") -> {
            delay(10_000)
            action()
          }
          else -> {
            systemLogger.error { "Http error ${e.response.status} ${e.response.request.url} $e \n$txt" }
            null
          }
        }
      }
      else -> {
        systemLogger.error { "Unknown error during HTTP request $e \n${e.stackTraceToString()}" }
        null
      }
    }
  }.getOrDefault(null)?.let { response ->
    if (response.status in setOf(HttpStatusCode.NotFound, HttpStatusCode.BadGateway)) {
      null
    } else response
  }
  
}

suspend inline fun <reified R> fetch(
  url: String,
  default: R,
  noinline config: HttpRequestBuilder.() -> Unit = {},
): R = fetch(url, config) ?: default

suspend inline fun <reified R> fetch(
  url: String,
  noinline config: HttpRequestBuilder.() -> Unit = {},
): R? = runCatching {
  request(url, config)?.readText()?.let { txt ->
    when {
      txt.contains("You've reached a dark spot...") -> {
        null
      }
      R::class == String::class -> txt as R
      else -> {
        try {
          prettyJson.decodeFromString<R>(txt)
        } catch (e: Exception) {
          systemLogger.error { "Error parsing ${R::class.qualifiedName} from [$txt]\n\t$e" }
          null
        }
      }
    }
  }
}.onFailure { e ->
  systemLogger.error { "Error parsing ${R::class.qualifiedName} from HttpResponse $e\n\t${e.stackTraceToString()}" }
}.getOrNull()
