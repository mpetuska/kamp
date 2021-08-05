package app.client.util

import androidx.compose.runtime.Composable
import dev.petuska.kmdc.requireJsModule
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.I
import org.w3c.dom.HTMLElement

private object CSS {
  private fun importFAModule(name: String) {
    requireJsModule("@fortawesome/fontawesome-free/js/$name.js")
  }

  init {
    importFAModule("brands")
    importFAModule("fontawesome")
  }
}

fun fab(icon: String): Array<String> {
  CSS
  return arrayOf("fab", "fa-$icon")
}

@Composable
fun FABIcon(
  icon: String,
  attrs: AttrBuilderContext<HTMLElement>? = null,
  content: ContentBuilder<HTMLElement>? = null,
) {
  I(
    attrs = {
      classes(*fab(icon))
      attrs?.invoke(this)
    },
    content = content,
  )
}
