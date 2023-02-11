package dev.petuska.kodex.client.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import dev.petuska.kodex.client.util.select

private val DarkThemeColors = darkColorScheme()
private val LightThemeColors = lightColorScheme()

@Composable
fun AppTheme(content: @Composable () -> Unit) {
  val darkTheme by select { darkTheme }
  val colorScheme = if (darkTheme) {
    DarkThemeColors
  } else {
    LightThemeColors
  }
  MaterialTheme(
    colorScheme = colorScheme,
    content = content
  )
}
