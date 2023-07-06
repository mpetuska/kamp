package dev.petuska.kodex.cli.cmd.capture

import com.github.ajalt.clikt.core.CliktCommand
import dev.petuska.kodex.cli.util.LoggerDelegate
import dev.petuska.kodex.repository.LibraryRepository
import dev.petuska.kodex.repository.StatisticRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class CaptureCmd :
  CliktCommand(name = "capture", help = "Capture a snapshot of current library distribution"),
  KoinComponent {

  private val libRepository: LibraryRepository by inject()
  private val statRepository: StatisticRepository by inject()
  private val logger by LoggerDelegate()
  private val json by inject<Json>(named("pretty"))

  override fun run() = runBlocking {
    logger.info("Extracting statistics...")
    val stats = libRepository.captureStatistics()
    logger.info("Persisting statistics...")
    statRepository.create(stats)
    logger.info("Done!\n" + json.encodeToString(stats))
  }
}
