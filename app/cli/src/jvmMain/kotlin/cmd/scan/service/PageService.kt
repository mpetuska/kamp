package dev.petuska.kamp.cli.cmd.scan.service

import dev.petuska.kamp.cli.cmd.scan.client.MavenRepositoryClient
import dev.petuska.kamp.cli.cmd.scan.domain.RepositoryItem
import dev.petuska.kamp.cli.util.toHumanString
import dev.petuska.kamp.core.util.logger
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.time.measureTime

class PageService(
  private val client: MavenRepositoryClient<*>,
) {
  private val logger = logger()
  private val pathSeparator = Regex("[\\./\\\\]")

  fun findPages(
    path: String = "",
    include: Collection<String>,
    exclude: Collection<String>,
  ): Flow<RepositoryItem.Directory> = channelFlow {
    producePages(RepositoryItem.Directory("", path), include, exclude)
  }

  private fun RepositoryItem.isIncluded(
    include: List<Pair<String, String?>>,
    exclude: List<Pair<String, String?>>,
  ): Triple<Boolean, Boolean, Boolean> {
    val safePath = name.removePrefix("/").removeSuffix("/")
    var explicit = false
    var explicitChildren = false
    val included = include.takeIf { it.isNotEmpty() }?.let { filter ->
      filter.any { (match, next) ->
        if (match.isNotBlank()) explicit = true
        if (next == null) explicitChildren = true
        if (next == null) {
          safePath.equals(match, ignoreCase = true)
        } else {
          safePath.startsWith(match, ignoreCase = true)
        }
      }
    } ?: true
    val excluded = exclude.takeIf { it.isNotEmpty() }?.let { filter ->
      filter.any { (match, next) ->
        if (next == null) {
          safePath.equals(match, ignoreCase = true)
        } else {
          safePath.startsWith(match, ignoreCase = true)
        }
      }
    } ?: false
    return Triple((included && !excluded), explicit, explicitChildren)
  }

  private fun Collection<String>.splitFirst(): List<Pair<String, String?>> = map {
    val s = it.split(pathSeparator, limit = 2)
    val next = s.getOrNull(1)
    s[0] to if (next?.isBlank() == true) null else (next ?: "")
  }

  private suspend fun ProducerScope<RepositoryItem.Directory>.producePages(
    parent: RepositoryItem.Directory,
    include: Collection<String>,
    exclude: Collection<String>,
    explicitChildren: Boolean = false,
  ) {
    supervisorScope {
      val cInclude = include.splitFirst()
      val cExclude = exclude.splitFirst()

      val items = client.listRepositoryPath(parent.path)
        ?.filterIsInstance<RepositoryItem.Directory>()
        ?.mapNotNull { item ->
          val (included, explicit, explicitC) = item.isIncluded(cInclude, cExclude)
          item.takeIf { included }?.let { Triple(it, explicit, explicitC) }
        }

      items?.map { (item, explicit, explicitC) ->
        logger.debug("Found page [${item.name}] in $parent")
        send(item)
        launch {
          if (explicit || explicitChildren) {
            logger.info("Scanning included page tree in $item")
          } else {
            logger.debug("Looking for pages in $item")
          }
          val duration = measureTime {
            producePages(
              parent = item,
              include = cInclude.mapNotNull(Pair<*, String?>::second),
              exclude = cExclude.mapNotNull(Pair<*, String?>::second),
              explicitChildren = explicitC,
            )
          }
          if (explicitChildren) {
            logger.info("Finished scanning included page tree at $item in ${duration.toHumanString()}")
          }
        }
      }
    }
  }
}
