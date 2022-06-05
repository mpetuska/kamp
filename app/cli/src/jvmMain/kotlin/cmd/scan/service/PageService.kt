package dev.petuska.kamp.cli.cmd.scan.service

import dev.petuska.kamp.cli.cmd.scan.client.MavenRepositoryClient
import dev.petuska.kamp.cli.cmd.scan.domain.RepositoryItem
import dev.petuska.kamp.core.util.logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

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
  ): Pair<Boolean, Boolean> {
    val safePath = name.removePrefix("/").removeSuffix("/")
    var explicit = false
    val included = include.takeIf { it.isNotEmpty() }?.let { filter ->
      filter.any { (match, next) ->
        if (match.isNotBlank()) explicit = true
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
    return (included && !excluded) to explicit
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
  ): List<Job> = supervisorScope {
    val cInclude = include.splitFirst()
    val cExclude = exclude.splitFirst()

    val items = client.listRepositoryPath(parent.path)
      ?.filterIsInstance<RepositoryItem.Directory>()
      ?.mapNotNull { item ->
        val (included, explicit) = item.isIncluded(cInclude, cExclude)
        item.takeIf { included }?.let { it to explicit }
      }

    items?.map { (item, explicit) ->
      logger.debug("Found page [${item.name}] in $parent")
      send(item)
      if (explicit) logger.info("Scanning included page tree in $item") else logger.debug("Looking for pages in $item")
      launch {
        producePages(
          parent = item,
          include = cInclude.mapNotNull(Pair<*, String?>::second),
          exclude = cExclude.mapNotNull(Pair<*, String?>::second)
        )
      }
    } ?: listOf()
  }
}
