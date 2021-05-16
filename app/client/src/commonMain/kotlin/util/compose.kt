package app.client.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.reduxkotlin.Store

@Composable
fun <TSlice, TState> Store<TState>.select(selector: TState.() -> TSlice): State<TSlice> {
  val result = remember { mutableStateOf(state.selector()) }
  DisposableEffect(Unit) {
    val unsubscribe = subscribe {
      result.value = state.selector()
    }
    onDispose(unsubscribe)
  }
  return result
}

@Composable
fun <TState> Store<TState>.select(): State<TState> = select { this }
