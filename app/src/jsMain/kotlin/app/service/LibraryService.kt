package app.service

import app.domain.*
import app.util.*
import io.ktor.client.*
import io.ktor.client.request.*
import kamp.domain.*

actual class LibraryService(private val client: HttpClient) {
  actual suspend fun getAll(page: Int, size: Int, search: String?): PagedResponse<KotlinMPPLibrary> =
    client.get("${path}?page=$page&size=$size${search?.let { "&search=$it" } ?: ""}".toApi())
  
  actual suspend fun getCount(): LibraryCount =
    client.get("${path}/count".toApi())
  
  actual companion object
}
