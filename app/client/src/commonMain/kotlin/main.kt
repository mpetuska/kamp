package app.client

import app.client.config.AppEnv
import app.client.config.loadDI
import app.client.config.loadEnv
import app.client.store.AppStore
import app.client.store.loadStore
import org.kodein.di.DI

data class AppContext(
  val args: Set<String>,
  val env: AppEnv,
  override val di: DI,
  val store: AppStore,
) : AppStore by store, DI by di

expect suspend fun AppContext.start()

suspend fun main(vararg args: String) {
  val argsSet = args.toSet()
  val env = loadEnv(argsSet)
  val di = loadDI(env)
  val store = loadStore(env)
  AppContext(
    args = argsSet,
    env = env,
    di = di,
    store = store
  ).start()
}
