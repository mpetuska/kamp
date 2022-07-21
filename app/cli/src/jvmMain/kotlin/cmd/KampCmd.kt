package dev.petuska.kamp.cli.cmd

import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import dev.petuska.kamp.cli.cmd.capture.CaptureCmd
import dev.petuska.kamp.cli.cmd.scan.ScanCmd
import org.kodein.di.DI
import org.kodein.di.DIAware

class KampCmd(override val di: DI) : NoOpCliktCommand(name = "kamp"), DIAware {
  init {
    subcommands(
      CompletionCommand(),
      ScanCmd(di),
      CaptureCmd(di),
    )
  }
}
