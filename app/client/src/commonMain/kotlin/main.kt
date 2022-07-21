package dev.petuska.kamp.client

import dev.petuska.kamp.client.config.AppEnv
import dev.petuska.kamp.client.config.loadDI
import dev.petuska.kamp.client.config.loadEnv
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.loadStore
import org.kodein.di.DI

data class AppContext(
  val args: List<String>,
  val env: AppEnv,
  override val di: DI,
  val store: AppStore,
) : AppStore by store, DI by di

expect suspend fun AppContext.start()

suspend fun main(vararg args: String) {
  val env = loadEnv(args = args)
  val store = loadStore(env)
  val di = loadDI(env, store)
  AppContext(
    args = args.toList(),
    env = env,
    di = di,
    store = store
  ).start()
}
