package dev.petuska.kodex.cli

import dev.petuska.kodex.cli.cmd.KodexCmd
import dev.petuska.kodex.cli.config.clientsModule
import dev.petuska.kodex.cli.config.cmdModule
import dev.petuska.kodex.cli.util.PrivateEnv
import dev.petuska.kodex.core.config.serialisationModule
import dev.petuska.kodex.repository.config.repositoryModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
  val koin = startKoin {
    slf4jLogger()
    modules(
      serialisationModule,
      repositoryModule(PrivateEnv.MONGO_STRING, PrivateEnv.MONGO_DATABASE),
      clientsModule,
      cmdModule,
    )
  }.koin
  koin.get<KodexCmd>().main(args)
  stopKoin()
}
