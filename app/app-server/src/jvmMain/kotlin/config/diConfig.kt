package dev.petuska.kodex.server.config

import dev.petuska.kodex.core.config.serialisationModule
import dev.petuska.kodex.repository.config.repositoryModule
import dev.petuska.kodex.server.util.PrivateEnv
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.diConfig() = install(Koin) {
  slf4jLogger()
  modules(
    serialisationModule,
    repositoryModule(PrivateEnv.MONGO_STRING, PrivateEnv.MONGO_DATABASE)
  )
}
