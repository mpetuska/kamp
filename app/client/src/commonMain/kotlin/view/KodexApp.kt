package dev.petuska.kodex.client.view

import androidx.compose.runtime.Composable
import dev.petuska.kodex.client.config.AppEnv
import dev.petuska.kodex.client.config.loadDI
import dev.petuska.kodex.client.store.AppStore
import dev.petuska.kodex.client.store.loadStore
import org.kodein.di.compose.withDI
import org.reduxkotlin.compose.StoreProvider

@Composable
fun KodexApp(env: AppEnv, content: @Composable AppStore.() -> Unit) {
  val store = loadStore(env)
  val di = loadDI(env, store)
  withDI(di) {
    StoreProvider(store, content = content)
  }
}
