package app.service

import app.util.*
import io.ktor.client.*
import io.ktor.client.request.*

actual class GreetService(private val client: HttpClient) {
  actual suspend fun greet(name: String?): String = client.get("/api/greet".toApi()) {
    parameter("name", name)
  }
}
