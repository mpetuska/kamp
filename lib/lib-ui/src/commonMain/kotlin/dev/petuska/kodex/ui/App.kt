package dev.petuska.kodex.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.petuska.kodex.client.store.action.AppAction
import dev.petuska.kodex.client.util.select
import org.reduxkotlin.compose.rememberDispatcher

@Composable
fun App() {
  val dispatch = rememberDispatcher()
  val count by select { count }
  val darkTheme by select { darkTheme }
  MaterialTheme {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      Column {
        Button({ dispatch(AppAction.SetDarkTheme(!darkTheme)) }) { Text("Switch Theme") }
        Button({ dispatch(AppAction.SetCount((count ?: 0) + 1L)) }) {
          Text("Hello $count")
        }
      }
    }
  }
}
