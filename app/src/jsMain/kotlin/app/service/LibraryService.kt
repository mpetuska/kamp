package app.service

import app.domain.*
import app.util.*
import io.ktor.client.*
import io.ktor.client.request.*
import kamp.domain.*

actual class LibraryService(private val client: HttpClient) {
  actual suspend fun getAll(page: Int, size: Int, search: String?, targets: Set<String>?): PagedResponse<KotlinMPPLibrary> {
    val pagination = "page=$page&size=$size"
    val searchQuery = search?.let { "&search=$it" } ?: ""
    val targetsQuery = targets?.joinToString(prefix = "&target=", separator = "&target=") ?: ""
    
    return client.get("${path}?$pagination$searchQuery$targetsQuery".toApi())
  }
  
  actual suspend fun getCount(): LibraryCount =
    client.get("${path}/count".toApi())
  
  actual companion object
}
