package dev.petuska.kodex.cli.cmd

import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import dev.petuska.kodex.cli.cmd.capture.CaptureCmd
import dev.petuska.kodex.cli.cmd.scan.ScanCmd
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class KodexCmd : NoOpCliktCommand(name = "kodex"), KoinComponent {
  init {
    subcommands(
      CompletionCommand(),
      get<ScanCmd>(),
      get<CaptureCmd>(),
    )
  }
}
