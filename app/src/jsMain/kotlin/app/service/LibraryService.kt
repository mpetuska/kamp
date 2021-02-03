package app.service

import app.domain.*
import io.ktor.client.*
import io.ktor.client.request.*
import kamp.domain.*
import kotlinx.serialization.json.*

actual class LibraryService(private val client: HttpClient, private val json: Json) {
  actual suspend fun getAll(page: Int, size: Int): PagedResponse<KotlinMPPLibrary> =
    client.get<String>("/api/library").let {
      json.decodeFromString(PagedResponse.serializer(KotlinMPPLibrary.serializer()), it)
    }
}
