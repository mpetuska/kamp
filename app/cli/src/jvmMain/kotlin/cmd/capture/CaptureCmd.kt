package dev.petuska.kamp.cli.cmd.capture

import com.github.ajalt.clikt.core.CliktCommand
import dev.petuska.kamp.core.util.logger
import dev.petuska.kamp.repository.LibraryRepository
import dev.petuska.kamp.repository.StatisticRepository
import kotlinx.coroutines.runBlocking
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class CaptureCmd(override val di: DI) : CliktCommand(name = "capture"), DIAware {
  private val logger = logger()
  private val libRepository by instance<LibraryRepository>()
  private val statRepository by instance<StatisticRepository>()

  override fun run() = runBlocking {
    logger.info("Extracting statistics...")
    val stats = libRepository.captureStatistics()
    logger.info("Persisting statistics...")
    statRepository.create(stats)
    logger.info("Done!")
  }
}
