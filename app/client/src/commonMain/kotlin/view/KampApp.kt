package dev.petuska.kamp.client.view

import androidx.compose.runtime.Composable
import org.kodein.di.DI
import org.kodein.di.compose.withDI

@Composable
fun KampApp(di: DI, content: @Composable () -> Unit) {
  withDI(di, content = content)
}
