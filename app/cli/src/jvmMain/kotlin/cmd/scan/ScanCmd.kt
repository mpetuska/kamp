package dev.petuska.kamp.cli.cmd.scan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import dev.petuska.kamp.cli.cmd.scan.domain.Repository
import dev.petuska.kamp.cli.cmd.scan.domain.SimpleMavenArtefact
import dev.petuska.kamp.cli.cmd.scan.service.PageService
import dev.petuska.kamp.cli.cmd.scan.service.SimpleMavenArtefactService
import dev.petuska.kamp.cli.util.toHumanString
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.domain.KotlinTarget
import dev.petuska.kamp.core.util.logger
import dev.petuska.kamp.repository.LibraryRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTime

class ScanCmd(
  override val di: DI
) : CliktCommand(name = "scan", help = "Scan a maven repository for kotlin libraries"), DIAware {
  private class FilterOptions : OptionGroup() {
    val from by option(help = "Repository root page filter start")
      .choice(('a'..'z').associateBy(Char::toString), ignoreCase = true).required()

    val to by option(help = "Repository root page filter end")
      .choice(('a'..'z').associateBy(Char::toString), ignoreCase = true).required()
  }

  private val repository by argument(help = "Repository alias to scan for")
    .choice(Repository.values().associateBy(Repository::alias), ignoreCase = true)

  private val include by option(help = "Repository root page filter to include")
    .multiple()

  private val exclude by option(help = "Repository root page filter to exclude")
    .multiple()

  private val excludeLetters by option(help = "Same as if you were to pass --exclude for a..z").flag()

  private val filterOptions by FilterOptions().cooccurring()

  private val delay by option(help = "Worker processing delay in milliseconds")
    .long().convert { it.milliseconds }

  private val workers by option(help = "Concurrent worker count")
    .int().default(Runtime.getRuntime().availableProcessors())

  // ===================================================================================================================

  private val logger = logger()
  private val libraryRepository by di.instance<LibraryRepository>()

  @OptIn(FlowPreview::class)
  override fun run() = runBlocking {
    val client = run {
      val json by di.instance<Json>("pretty")
      val httpClient by di.instance<HttpClient>()
      repository.client(repository.url, httpClient, json)
    }
    logger.info("Scanning ${repository.alias} repository")

    val includes = run {
      include + (filterOptions?.run { from..to }?.map(Char::toString) ?: listOf())
    }
    val excludes = exclude + (if (excludeLetters) ('a'..'z').map(Char::toString) else listOf())

    logger.info("Bootstrapping repository page lookup")
    val pages = PageService(
      client = client,
    ).findPages(include = includes, exclude = excludes)
    logger.info("Bootstrapping maven artefact lookup")
    val scanner = SimpleMavenArtefactService(
      workers = workers,
      delay = delay,
      client = client,
    )
    val duration = measureTime {
      var count = 0
      val artefacts: Flow<SimpleMavenArtefact> = scanner.findMavenArtefacts(pages.buffer().produceIn(this))
      val libraries: Flow<KotlinLibrary> = scanner.findKotlinLibraries(artefacts)
      libraries.buffer().collect {
        logger.info("Found kotlin library: ${it._id} ${it.targets.map(KotlinTarget::id)}")
        count++
        launch { libraryRepository.create(it) }
      }
      logger.info(
        "Found $count kotlin libraries with gradle metadata in ${repository.alias} repository " +
          "filtered by $includes, explicitly excluding $excludes."
      )
      client.close()
    }
    logger.info(
      "Finished scanning ${repository.alias} in ${duration.toHumanString()}"
    )
  }
}
