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
  private val include: Collection<String>?,
  private val exclude: Collection<String>?,
) {
  private val logger = logger()

  fun findPages(path: String = ""): Flow<RepositoryItem.Directory> = channelFlow {
    val directories = client.listRepositoryPath(path)?.filter { item ->
      val safePath = item.path.removePrefix("/")
      val included =
        include?.let { filter -> filter.any { safePath.startsWith(it) } } ?: true
      val excluded =
        exclude?.let { filter -> filter.any { safePath.startsWith(it) } } ?: false
      included && !excluded
    }?.filterIsInstance<RepositoryItem.Directory>()
    directories?.forEach { page ->
      send(page)
      producePages(page)
    }
  }

  private suspend fun ProducerScope<RepositoryItem.Directory>.producePages(
    parent: RepositoryItem.Directory
  ): List<Job> = supervisorScope {
    logger.debug("Looking for pages in $parent")
    val items = client.listRepositoryPath(parent.path)
    items?.filterIsInstance<RepositoryItem.Directory>()?.map { item ->
      logger.debug("Found page [$item] in $parent")
      send(item)
      launch {
        producePages(item)
      }
    } ?: listOf()
  }
}
