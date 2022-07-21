package dev.petuska.kamp.server.config

import dev.petuska.kamp.core.config.serialisation
import dev.petuska.kamp.repository.config.repositoryDI
import dev.petuska.kamp.server.util.PrivateEnv
import io.ktor.server.application.Application
import org.kodein.di.ktor.di

fun Application.diConfig() = di {
  import(serialisation)
  import(repositoryDI(PrivateEnv.MONGO_STRING, PrivateEnv.MONGO_DATABASE))
}
