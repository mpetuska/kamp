package dev.petuska.kodex.client

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import dev.petuska.kodex.client.config.loadEnv
import dev.petuska.kodex.client.store.action.AppAction
import dev.petuska.kodex.client.view.App
import dev.petuska.kodex.client.view.AppTheme
import dev.petuska.kodex.client.view.KodexApp
import java.awt.Dimension
import java.awt.Toolkit

private fun getPreferredWindowSize(desiredWidth: Int, desiredHeight: Int): DpSize {
  val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
  val preferredWidth: Int = (screenSize.width * 0.8f).toInt()
  val preferredHeight: Int = (screenSize.height * 0.8f).toInt()
  val width: Int = if (desiredWidth < preferredWidth) desiredWidth else preferredWidth
  val height: Int = if (desiredHeight < preferredHeight) desiredHeight else preferredHeight
  return DpSize(width.dp, height.dp)
}

@Composable
fun icAppRounded() = painterResource("images/android-chrome-512x512.png")

suspend fun main(vararg args: String) {
  val env = loadEnv(args = args)
  application {
    Window(
      onCloseRequest = ::exitApplication,
      title = "Kamp",
      state = WindowState(
        position = WindowPosition.Aligned(Alignment.Center),
        size = getPreferredWindowSize(800, 300)
      ),
      undecorated = false,
      icon = icAppRounded(),
    ) {
      KodexApp(env) {
        dispatch(AppAction.SetDarkTheme(isSystemInDarkTheme()))
        AppTheme {
          App()
        }
      }
    }
  }
}
