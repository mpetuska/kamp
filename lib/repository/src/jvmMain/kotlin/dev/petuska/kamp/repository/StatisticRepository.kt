package dev.petuska.kamp.repository

import dev.petuska.kamp.core.domain.LibrariesStatistic
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

    val dbCall = query?.let(collection::find) ?: collection.find()
    dbCall.skip(size * (page - 1)).limit(size)
    return dbCall.toList()
  }

  suspend fun count(from: Long?, to: Long?): Long {
    return collection.countDocuments(buildQuery(from, to) ?: "{}")
  }

  suspend fun getByDate(date: String): LibrariesStatistic? {
    return collection.findOneById(date)
  }

  suspend fun create(statistic: LibrariesStatistic) {
    collection.save(statistic)
  }
}
