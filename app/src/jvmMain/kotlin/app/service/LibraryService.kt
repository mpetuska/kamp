package app.service

import app.domain.*
import app.util.*
import io.ktor.application.*
import kamp.domain.*
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.*

actual class LibraryService(
  private val call: ApplicationCall,
  private val collection: CoroutineCollection<KotlinMPPLibrary>,
) {
  actual suspend fun getAll(page: Int, size: Int): PagedResponse<KotlinMPPLibrary> {
    val x = collection.find().sort(ascending(KotlinMPPLibrary::name)).skip(size * (page - 1)).limit(size).toList()
    return PagedResponse(
      data = x,
      next = call.request.buildNextUrl(x.size),
      prev = call.request.buildPrevUrl()
    )
  }
  
  suspend fun create(library: KotlinMPPLibrary) {
    collection.save(library)
  }
}
