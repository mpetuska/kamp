package app.server.service

import app.server.util.buildNextUrl
import app.server.util.buildPrevUrl
import domain.KotlinMPPLibrary
import domain.LibraryCount
import domain.PagedResponse
import io.ktor.application.ApplicationCall
import org.litote.kmongo.MongoOperator.all
import org.litote.kmongo.MongoOperator.and
import org.litote.kmongo.MongoOperator.language
import org.litote.kmongo.MongoOperator.meta
import org.litote.kmongo.MongoOperator.search
import org.litote.kmongo.MongoOperator.text
import org.litote.kmongo.bson
import org.litote.kmongo.coroutine.CoroutineCollection
import service.LibraryService

class LibraryServiceImpl(
  private val call: ApplicationCall,
  private val collection: CoroutineCollection<KotlinMPPLibrary>,
) : LibraryService {
  private fun buildQuery(_search: String?, targets: Set<String>?): Pair<String?, String?> {
    val searchQuery = _search?.let {
      """
      {
        $text: {
          $search: '${_search.replace("'", "\\'")}',
          $language: 'en'
        }
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
    val finalQuery = setOfNotNull(searchQuery, targetsQuery).takeIf { it.isNotEmpty() }?.let {
      """
      {
        $and: [${it.joinToString(",")}]
      }
      """.trimIndent()
    }

    val projection = _search?.let {
      """
        { score: {  $meta: "textScore" } }
      """.trimIndent()
    }

    return finalQuery to projection
  }

  override suspend fun getAll(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
    onProgress: (suspend (current: Long, total: Long) -> Unit)?,
  ): PagedResponse<KotlinMPPLibrary> {
    val (query, projection) = buildQuery(search, targets)

    val dbCall = query?.let { collection.find(it) } ?: collection.find()
    projection?.let {
      dbCall.projection(it.bson)
      dbCall.sort("""{ score: { $meta: "textScore" } }""".bson)
    } ?: dbCall.ascendingSort(KotlinMPPLibrary::name)
    dbCall.skip(size * (page - 1))
      .limit(size)
    val data = dbCall.toList()
    return PagedResponse(
      data = data,
      page = page,
      next = call.request.buildNextUrl(data.size),
      prev = call.request.buildPrevUrl()
    )
  }

  override suspend fun getCount(search: String?, targets: Set<String>?): LibraryCount {
    return LibraryCount(collection.countDocuments(buildQuery(search, targets).first ?: "{}"))
  }

  override suspend fun create(library: KotlinMPPLibrary) {
    collection.save(library)
  }
}