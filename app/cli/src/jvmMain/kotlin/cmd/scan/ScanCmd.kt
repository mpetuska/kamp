package dev.petuska.kamp.cli.cmd.scan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.long
import dev.petuska.kamp.cli.cmd.scan.domain.FileData
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.onEach
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
    val from by option(help = "Repository root page filter start").choice(
      ('a'..'z').associateBy(Char::toString),
      ignoreCase = true
    ).required()

    val to by option(help = "Repository root page filter end").choice(
      ('a'..'z').associateBy(Char::toString),
      ignoreCase = true
    ).required()
  }

  private val repository by argument(help = "Repository alias to scan for").choice(
    Repository.values().associateBy(Repository::alias), ignoreCase = true
  )

  private val include by option(help = "Repository root page filter to include").multiple()

  private val exclude by option(help = "Repository root page filter to exclude").multiple()

  private val excludeLetters by option(help = "Same as if you were to pass --exclude for a..z").flag()

  private val delay by option(help = "Worker processing delay in milliseconds").long().convert { it.milliseconds }
    .default(2.milliseconds)

  private val filterOptions by FilterOptions().cooccurring()

  // ===================================================================================================================

  private val logger = logger()
  private val libraryRepository by di.instance<LibraryRepository>()

  override fun run() = runBlocking {
    val client = run {
      val json by di.instance<Json>("pretty")
      val httpClient by di.instance<HttpClient>()
      repository.client(repository.url, httpClient, json)
    }
    logger.info("Scanning ${repository.alias} repository")

    val includes = include.plus(filterOptions?.run { from..to }?.map(Char::toString) ?: listOf())
    val excludes = exclude.plus(if (excludeLetters) ('a'..'z').map(Char::toString) else listOf())

    logger.info("Bootstrapping repository page lookup")
    var pageCount = 0
    val pages = PageService(
      client = client,
      delay = delay,
    ).findPages(include = includes, exclude = excludes).buffer().onEach { pageCount++ }
    logger.info("Bootstrapping maven artefact lookup")
    val scanner = SimpleMavenArtefactService(
      client = client,
    )
    var libCount = 0
    val duration = measureTime {
      coroutineScope {
        val artefacts: Flow<FileData<SimpleMavenArtefact>> = scanner.findMavenArtefacts(pages).buffer()
        val libraries: Flow<FileData<KotlinLibrary>> = scanner.findKotlinLibraries(artefacts).buffer()
        libraries.collect { (_, lib) ->
          logger.info("Found kotlin library: ${lib._id} ${lib.targets.map(KotlinTarget::id)}")
          libCount++
          launch { libraryRepository.create(lib) }
        }
        client.close()
      }
    }
    val filterMsg = " filtered by $includes, explicitly excluding $excludes."
    logger.info(
      "Finished scanning ${repository.alias} in ${duration.toHumanString()} and scanned $pageCount subpages" + filterMsg
    )
    logger.info(
      "Found $libCount kotlin libraries with gradle metadata in ${repository.alias} repository" + filterMsg
    )
  }
}
