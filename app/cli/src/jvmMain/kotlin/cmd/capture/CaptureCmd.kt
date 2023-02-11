package dev.petuska.kodex.cli.cmd.capture

import com.github.ajalt.clikt.core.CliktCommand
import dev.petuska.kodex.core.util.logger
import dev.petuska.kodex.repository.LibraryRepository
import dev.petuska.kodex.repository.StatisticRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class CaptureCmd(
  override val di: DI
) : CliktCommand(name = "capture", help = "Capture a snapshot of current library distribution"),
  DIAware {
  private val logger = logger()
  private val libRepository by instance<LibraryRepository>()
  private val statRepository by instance<StatisticRepository>()
  private val json by instance<Json>("pretty")

  override fun run() = runBlocking {
    logger.info("Extracting statistics...")
    val stats = libRepository.captureStatistics()
    logger.info("Persisting statistics...")
    statRepository.create(stats)
    logger.info("Done!\n" + json.encodeToString(stats))
  }
}
