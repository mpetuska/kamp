package dev.petuska.kodex.cli

import dev.petuska.kodex.cli.cmd.KodexCmd
import dev.petuska.kodex.cli.config.sharedDI
import dev.petuska.kodex.core.di.ApplicationDIScope

fun main(args: Array<String>) {
  val di = sharedDI()
  KodexCmd(di).main(args)
  ApplicationDIScope.close()
}
