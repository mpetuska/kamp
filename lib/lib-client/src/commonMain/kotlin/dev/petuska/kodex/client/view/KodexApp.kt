package dev.petuska.kodex.client.view

import androidx.compose.runtime.Composable
import dev.petuska.kodex.client.config.AppEnv
import dev.petuska.kodex.client.config.startDI
import dev.petuska.kodex.client.store.AppStore
import dev.petuska.kodex.client.store.loadStore
import org.reduxkotlin.compose.StoreProvider

@Composable
fun KodexApp(env: AppEnv, content: @Composable AppStore.() -> Unit) {
  val store = loadStore(env)
  startDI(env, store)
  StoreProvider(store, content = content)
}
