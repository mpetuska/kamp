package dev.petuska.kodex.core.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serialisationModule = module {
  single(named("pretty")) {
    Json {
      prettyPrint = true
      ignoreUnknownKeys = true
    }
  }
  single { Json { ignoreUnknownKeys = true } }
  @OptIn(ExperimentalSerializationApi::class)
  single { Cbor { ignoreUnknownKeys = true } }
}
