package app.service

import io.ktor.client.*
import io.ktor.client.request.*

actual class GreetService(private val client: HttpClient) {
  actual suspend fun greet(name: String?): String = client.get("/api/greet") {
    parameter("name", name)
  }
}
