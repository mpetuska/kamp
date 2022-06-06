package dev.petuska.kamp.cli.cmd.scan.service

import dev.petuska.kamp.cli.cmd.scan.client.MavenRepositoryClient
import dev.petuska.kamp.cli.cmd.scan.domain.RepoDirectory
import dev.petuska.kamp.cli.cmd.scan.domain.RepoItem
import dev.petuska.kamp.cli.cmd.scan.domain.RepoItem.Companion.SEP
import dev.petuska.kamp.cli.util.toHumanString
import dev.petuska.kamp.core.util.logger
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.supervisorScope
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.measureTime

class PageService(
  private val client: MavenRepositoryClient<*>,
  private val delay: Duration,
) {
  private val logger = logger()
  private val pathSeparator = Regex("[\\.$SEP\\\\]")

  fun findPages(
    include: Collection<String>,
    exclude: Collection<String>,
    path: String = "",
  ): Flow<RepoDirectory.Listed> = channelFlow {
    scanPage(
      RepoDirectory.fromPath(path),
      include.map { it.removePrefix(SEP) },
      exclude.map { it.removePrefix(SEP) },
    )
  }

  private fun RepoItem.isIncluded(
    include: List<Pair<String, String?>>,
    exclude: List<Pair<String, String?>>,
  ): Triple<Boolean, Boolean, Boolean> {
    var explicit = false
    var explicitChildren = false
    val included = include.takeIf { it.isNotEmpty() }?.let { filter ->
      filter.any { (match, next) ->
        if (match.isNotBlank()) explicit = true
        if (next == null) explicitChildren = true
        if (next == null) {
          absolutePath.equals(match, ignoreCase = true)
        } else {
          absolutePath.startsWith(match, ignoreCase = true)
        }
      }
    } ?: true
    val excluded = exclude.takeIf { it.isNotEmpty() }?.let { filter ->
      filter.any { (match, next) ->
        next.isNullOrBlank() && absolutePath.startsWith(match, ignoreCase = true)
      }
    } ?: false
    return Triple((included && !excluded), explicit, explicitChildren)
  }

  private fun Collection<String>.splitFirst(): List<Pair<String, String?>> = map {
    val s = it.split(pathSeparator, limit = 2)
    val next = s.getOrNull(1)
    "$SEP${s[0]}" to if (next?.isBlank() == true) null else (next ?: "")
  }

  private suspend fun ProducerScope<RepoDirectory.Listed>.scanPage(
    page: RepoDirectory,
    include: Collection<String>,
    exclude: Collection<String>,
    explicitChildren: Boolean = false,
  ): Long {
    val (counts, children) = supervisorScope {
      val cInclude = include.splitFirst()
      val cExclude = exclude.splitFirst()
      val includes = cInclude.map {
        page.item(it.first).absolutePath to it.second
      }
      val excludes = cExclude.map {
        page.item(it.first).absolutePath to it.second
      }

      val items = client.listRepositoryPath(page) ?: listOf()
      send(page.list(items))
      val directories = items.filterIsInstance<RepoDirectory>().mapNotNull { item ->
        val (included, explicit, explicitC) = item.isIncluded(includes, excludes)
        item.takeIf { included }?.let { Triple(it, explicit, explicitC) }
      }

      directories.map { (item, explicit, explicitC) ->
        logger.debug("Found page [${item.name}] in $page")
        async {
          if (explicit || explicitChildren) {
            logger.info("Scanning included page tree in $item")
          } else {
            logger.debug("Looking for pages in $item")
          }
          var count by Delegates.notNull<Long>()
          val duration = measureTime {
            count = scanPage(
              page = item,
              include = cInclude.mapNotNull { it.second?.takeIf(String::isNotBlank) },
              exclude = cExclude.mapNotNull { it.second?.takeIf(String::isNotBlank) },
              explicitChildren = explicitC,
            )
          }
          if (explicit || explicitChildren) {
            logger.info(
              "Finished scanning included page tree at $item in ${duration.toHumanString()} and found $count subpages"
            )
          }
          count
        }.also { delay(delay) }
      } to directories.size
    }
    return counts.awaitAll().sum() + children
  }
}
