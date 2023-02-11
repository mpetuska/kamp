package dev.petuska.kodex.client.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import dev.petuska.kodex.client.store.state.AppState
import org.reduxkotlin.compose.selectState

@Composable
inline fun <TSlice> select(
  crossinline selector: @DisallowComposableCalls AppState.() -> TSlice
) = selectState(selector)
