package dev.petuska.kodex.client

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dev.petuska.kodex.client.config.loadEnv
import dev.petuska.kodex.client.view.App
import dev.petuska.kodex.client.view.AppTheme
import dev.petuska.kodex.client.view.KodexApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  private val scope = MainScope()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    scope.launch {
      val env = loadEnv()
      setContent {
        KodexApp(env) {
          AppTheme {
            App()
          }
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    scope.cancel()
  }
}
