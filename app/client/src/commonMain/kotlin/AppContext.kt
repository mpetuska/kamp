package dev.petuska.kodex.client

import dev.petuska.kodex.client.config.AppEnv
import dev.petuska.kodex.client.store.AppStore
import org.kodein.di.DI

data class AppContext(
  val args: List<String>,
  val env: AppEnv,
  override val di: DI,
  override val store: AppStore,
) : AppStore by store, DI by di
