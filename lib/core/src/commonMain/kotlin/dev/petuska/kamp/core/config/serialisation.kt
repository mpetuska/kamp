package dev.petuska.kamp.core.config

import dev.petuska.kamp.core.util.DIModule
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import org.kodein.di.bindSingleton

val serialisation by DIModule {
  bindSingleton("pretty") {
    Json {
      prettyPrint = true
      ignoreUnknownKeys = true
    }
  }
  bindSingleton { Json { ignoreUnknownKeys = true } }
  bindSingleton { Cbor { ignoreUnknownKeys = true } }
}
