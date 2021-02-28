package app.service

import app.domain.*
import app.util.*
import io.ktor.application.*
import kamp.domain.*
import org.litote.kmongo.*
import org.litote.kmongo.MongoOperator.or
import org.litote.kmongo.MongoOperator.regex
import org.litote.kmongo.coroutine.*

actual class LibraryService(
  private val call: ApplicationCall,
  private val collection: CoroutineCollection<KotlinMPPLibrary>,
) {
  actual suspend fun getAll(page: Int, size: Int, search: String?): PagedResponse<KotlinMPPLibrary> {
    val query = search?.let {
      """
      {
        $or: [
         {name: {$regex: /$search/i}},
         {group: {$regex: /$search/i}},
         {description: {$regex: /$search/i}},
        ],
      }
      """.trimIndent()
    } ?: "{}"
    val x = collection.find(query).sort(ascending(KotlinMPPLibrary::name)).skip(size * (page - 1)).limit(size).toList()
    return PagedResponse(
      data = x,
      page = page,
      next = call.request.buildNextUrl(x.size),
      prev = call.request.buildPrevUrl()
    )
  }
  
  suspend fun create(library: KotlinMPPLibrary) {
    collection.save(library)
  }
}
