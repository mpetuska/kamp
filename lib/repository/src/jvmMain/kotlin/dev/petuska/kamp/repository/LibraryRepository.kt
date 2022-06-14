package dev.petuska.kamp.repository

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Facet
import com.mongodb.client.model.Field
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.KotlinTarget
import dev.petuska.kamp.core.domain.LibrariesStatistic
import dev.petuska.kamp.repository.util.runCatchingIO
import org.litote.kmongo.MongoOperator.all
import org.litote.kmongo.MongoOperator.and
import org.litote.kmongo.MongoOperator.arrayElemAt
import org.litote.kmongo.MongoOperator.language
import org.litote.kmongo.MongoOperator.meta
import org.litote.kmongo.MongoOperator.search
import org.litote.kmongo.addFields
import org.litote.kmongo.bson
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.aggregate
import org.litote.kmongo.facet
import java.awt.SystemColor.text
import java.text.SimpleDateFormat
import java.util.Date

class LibraryRepository(private val collection: CoroutineCollection<KotlinLibrary>) {
  private fun buildQuery(querySearch: String?, targets: Set<String>?): Pair<String?, String?> {
    val searchQuery = querySearch?.let {
      """
      {
        $text: {
          $search: '${querySearch.replace("'", "\\'")}',
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

    val projection = querySearch?.let {
      """
        { score: {  $meta: "textScore" } }
      """.trimIndent()
    }

    return finalQuery to projection
  }

  suspend fun search(
    page: Int,
    size: Int,
    search: String?,
    targets: Set<String>?,
  ): List<KotlinLibrary> {
    val (query, projection) = buildQuery(search, targets)

    return runCatchingIO {
      val dbCall = query?.let(collection::find) ?: collection.find()
      projection?.let {
        dbCall.projection(it.bson)
        dbCall.sort("""{ score: { $meta: "textScore" } }""".bson)
      } ?: dbCall.ascendingSort(KotlinLibrary::name)
      dbCall.skip(size * (page - 1)).limit(size)
      dbCall.toList()
    }.getOrThrow()
  }

  suspend fun count(search: String?, targets: Set<String>?): Long {
    return runCatchingIO { collection.countDocuments(buildQuery(search, targets).first ?: "{}") }.getOrThrow()
  }

  suspend fun create(library: KotlinLibrary) {
    runCatchingIO { collection.save(library) }.getOrThrow()
  }

  suspend fun findById(id: String): KotlinLibrary? {
    return runCatchingIO { collection.findOneById(id) }.getOrThrow()
  }

  @Suppress("LongMethod")
  suspend fun captureStatistics(): LibrariesStatistic {
    val categories = listOf(
      KotlinTarget.Common.category,
      KotlinTarget.JVM.category,
      KotlinTarget.JS.category,
      KotlinTarget.Wasm.category,
      KotlinTarget.Native.category,
      KotlinTarget.Unknown.category,
    )
    val families = KotlinTarget.values().map(KotlinTarget::family).toSet() +
      KotlinTarget.Unknown("unknown").family
    val platforms = KotlinTarget.values().map(KotlinTarget::id)
    val date = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    val facets = buildList {
      add(Facet("total", Aggregates.count()))
      categories.map {
        Facet("c$it", Aggregates.match("{ 'targets.category': '$it' }".bson), Aggregates.count())
      }.let(::addAll)
      families.map {
        Facet("f$it", Aggregates.match("{ 'targets.family': '$it' }".bson), Aggregates.count())
      }.let(::addAll)
      platforms.map {
        Facet("p$it", Aggregates.match("{ 'targets.id': '$it' }".bson), Aggregates.count())
      }.let(::addAll)
    }
    val categoriesProjection = categories.joinToString(
      prefix = "{",
      postfix = "}",
      separator = ","
    ) { "'$it': { '$arrayElemAt': ['\$c$it.count', 0] }" }
    val familiesProjection = families.joinToString(
      prefix = "{",
      postfix = "}",
      separator = ","
    ) { "'$it': { '$arrayElemAt': ['\$f$it.count', 0] }" }
    val platformsProjection = platforms.joinToString(
      prefix = "{",
      postfix = "}",
      separator = ","
    ) { "'$it': { '$arrayElemAt': ['\$p$it.count', 0] }" }
    val projection = Aggregates.project(
      """
        {
          countTotal: { "$arrayElemAt": ["${'$'}total.count", 0] },
          countByCategory: $categoriesProjection,
          countByFamily: $familiesProjection,
          countByPlatform: $platformsProjection,
        }
      """.trimIndent().bson
    )
    return runCatchingIO {
      collection.aggregate<LibrariesStatistic>(
        facet(facets),
        projection,
        addFields(
          Field("date", dateFormat.format(date)),
          Field("ts", date.time),
        )
      ).first()!!
    }.getOrThrow()
  }
}
