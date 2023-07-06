package dev.petuska.kodex.repository

import dev.petuska.kodex.core.domain.LibrariesStatistic
import dev.petuska.kodex.repository.util.runCatchingIO
import org.litote.kmongo.MongoOperator.and
import org.litote.kmongo.MongoOperator.gte
import org.litote.kmongo.MongoOperator.lt
import org.litote.kmongo.coroutine.CoroutineCollection

class StatisticRepository(
  val collection: CoroutineCollection<LibrariesStatistic>,
) {
  private fun buildQuery(from: Long?, to: Long?): String? {
    val fromQuery = from?.let {
      """
        {
          ts: { $gte: $it }
        }
      """.trimIndent()
    }
    val toQuery = to?.let {
      """
        {
          ts: { $lt: $it }
        }
      """.trimIndent()
    }
    val finalQuery = setOfNotNull(fromQuery, toQuery).takeIf { it.isNotEmpty() }?.let {
      """
      {
        $and: [${it.joinToString(",")}]
      }
      """.trimIndent()
    }

    return finalQuery
  }

  suspend fun search(page: Int, size: Int, from: Long?, to: Long?): List<LibrariesStatistic> {
    val query = buildQuery(from, to)

    return runCatchingIO {
      val dbCall = query?.let(collection::find) ?: collection.find()
      dbCall.skip(size * (page - 1)).limit(size)
      dbCall.toList()
    }.getOrThrow()
  }

  suspend fun count(from: Long?, to: Long?): Long {
    return runCatchingIO { collection.countDocuments(buildQuery(from, to) ?: "{}") }.getOrThrow()
  }

  suspend fun findByDate(date: String): LibrariesStatistic? {
    return runCatchingIO { collection.findOneById(date) }.getOrThrow()
  }

  suspend fun create(statistic: LibrariesStatistic) {
    runCatchingIO { collection.save(statistic) }.getOrThrow()
  }
}
