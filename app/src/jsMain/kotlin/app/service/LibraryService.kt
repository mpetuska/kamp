package app.service

import app.domain.*
import app.util.*
import io.ktor.client.*
import io.ktor.client.request.*
import kamp.domain.*

actual class LibraryService(private val client: HttpClient) {
  actual suspend fun getAll(page: Int, size: Int): PagedResponse<KotlinMPPLibrary> =
    client.get("/api/library?page=$page&size=$size".toApi())
}
