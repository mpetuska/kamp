package dev.petuska.kodex.cli.cmd

import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import dev.petuska.kodex.cli.cmd.capture.CaptureCmd
import dev.petuska.kodex.cli.cmd.scan.ScanCmd
import org.kodein.di.DI
import org.kodein.di.DIAware

class KodexCmd(override val di: DI) : NoOpCliktCommand(name = "kodex"), DIAware {
  init {
    subcommands(
      CompletionCommand(),
      ScanCmd(di),
      CaptureCmd(di),
    )
  }
}
