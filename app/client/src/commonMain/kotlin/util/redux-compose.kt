package dev.petuska.kamp.client.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.petuska.kamp.client.store.AppStore
import dev.petuska.kamp.client.store.state.AppState
import org.kodein.di.compose.rememberInstance
import org.reduxkotlin.Store

@Composable
inline fun <TSlice, TState> Store<TState>.select(
  crossinline selector: @DisallowComposableCalls TState.() -> TSlice
): State<TSlice> {
  val result = remember { mutableStateOf(state.selector()) }
  DisposableEffect(result) {
    val unsubscribe = subscribe {
      result.value = state.selector()
    }
    onDispose(unsubscribe)
  }
  return result
}

@Composable
fun <TState> Store<TState>.select(): State<TState> = select { this }

@Composable
inline fun <TSlice> select(
  crossinline selector: @DisallowComposableCalls AppState.() -> TSlice
): State<TSlice> {
  val store by rememberInstance<AppStore>()
  return store.select(selector)
}

@Composable
fun <TSlice> select(): State<AppState> = select { this }
