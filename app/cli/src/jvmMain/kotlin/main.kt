package dev.petuska.kamp.cli

import dev.petuska.kamp.cli.cmd.KampCmd
import dev.petuska.kamp.cli.config.sharedDI
import dev.petuska.kamp.core.di.ApplicationDIScope

fun main(args: Array<String>) {
  val di = sharedDI()
  KampCmd(di).main(args)
  ApplicationDIScope.close()
}
