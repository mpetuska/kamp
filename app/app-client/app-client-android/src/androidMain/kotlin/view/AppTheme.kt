package dev.petuska.kodex.client.view

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import dev.petuska.kodex.client.util.select

private val LightThemeColors = lightColorScheme()
private val DarkThemeColors = darkColorScheme()

@Composable
fun AppTheme(content: @Composable () -> Unit) {
  val darkTheme by select { darkTheme }
  val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
  val colorScheme = when {
    dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
    dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
    darkTheme -> DarkThemeColors
    else -> LightThemeColors
  }
  MaterialTheme(
    colorScheme = colorScheme,
    content = content
  )
}
