package dev.petuska.kodex.server.config

import dev.petuska.kodex.core.config.serialisation
import dev.petuska.kodex.repository.config.repositoryDI
import dev.petuska.kodex.server.util.PrivateEnv
import io.ktor.server.application.Application
import org.kodein.di.ktor.di

fun Application.diConfig() = di {
  import(serialisation)
  import(repositoryDI(PrivateEnv.MONGO_STRING, PrivateEnv.MONGO_DATABASE))
}
