package dev.petuska.kamp.cli

import dev.petuska.kamp.cli.cmd.KampCmd
import dev.petuska.kamp.cli.config.sharedDI
import dev.petuska.kamp.core.util.cancelOnShutdown
import kotlinx.coroutines.Dispatchers

fun main(args: Array<String>) = cancelOnShutdown(Dispatchers.Default) {
  KampCmd(sharedDI()).main(args)
}
