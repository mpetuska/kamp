package app.service

import app.domain.*
import app.util.*
import io.ktor.application.*
import kamp.domain.*
import org.litote.kmongo.*
import org.litote.kmongo.MongoOperator.all
import org.litote.kmongo.MongoOperator.and
import org.litote.kmongo.MongoOperator.or
import org.litote.kmongo.MongoOperator.regex
import org.litote.kmongo.coroutine.*

actual class LibraryService(
  private val call: ApplicationCall,
  private val collection: CoroutineCollection<KotlinMPPLibrary>,
) {
  actual suspend fun getAll(page: Int, size: Int, search: String?, targets: Set<String>?): PagedResponse<KotlinMPPLibrary> {
    val searchQuery = search?.let {
      """
      {
        $or: [
         {name: {$regex: /$search/i}},
         {group: {$regex: /$search/i}},
         {description: {$regex: /$search/i}},
        ]
      }
      """.trimIndent()
    }
    val targetsQuery = targets?.let {
      """
      {
        'targets.platform': {
          $all: [${targets.joinToString(",") { "'$it'" }}]
        }
      }
      """.trimIndent()
    }
    val query = setOfNotNull(searchQuery, targetsQuery).takeIf { it.isNotEmpty() }?.let {
      """
      {
        $and: [${it.joinToString(",")}]
      }
      """.trimIndent()
    } ?: "{}"
    val data = collection.find(query).sort(ascending(KotlinMPPLibrary::name)).skip(size * (page - 1)).limit(size).toList()
    return PagedResponse(
      data = data,
      page = page,
      next = call.request.buildNextUrl(data.size),
      prev = call.request.buildPrevUrl()
    )
  }
  
  actual suspend fun getCount(): LibraryCount {
    return LibraryCount(collection.countDocuments())
  }
  
  suspend fun create(library: KotlinMPPLibrary) {
    collection.save(library)
  }
  
  actual companion object
}
