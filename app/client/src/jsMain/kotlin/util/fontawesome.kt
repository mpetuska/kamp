package dev.petuska.kodex.client.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.I
import org.w3c.dom.HTMLElement

@JsModule("@fortawesome/fontawesome-svg-core")
private external object FACore {
  interface FAModule

  interface FACoreLibrary {
    fun add(vararg modules: FAModule)
  }

  interface FACoreDom {
    fun watch()
  }

  val library: FACoreLibrary
  val dom: FACoreDom
}

@JsModule("@fortawesome/free-solid-svg-icons")
private external object FASolid {
  val fas: FACore.FAModule
}

@JsModule("@fortawesome/free-regular-svg-icons")
private external object FARegular {
  val far: FACore.FAModule
}

@JsModule("@fortawesome/free-brands-svg-icons")
private external object FABrands {
  val fab: FACore.FAModule
  val faGithub: FACore.FAModule
}

private val FAInitializer by lazy {
  FACore.library.add(FABrands.fab, FABrands.faGithub)

  FACore.dom.watch()
}

// private object CSS {
//   private fun importFAModule(name: String) {
//     requireJsModule("@fortawesome/fontawesome-free/js/$name.js")
//   }
//
//   init {
//     importFAModule("brands")
//     importFAModule("fontawesome")
//   }
// }

fun fab(icon: String): Array<String> {
  FAInitializer
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
      classes(classes = fab(icon))
      attrs?.invoke(this)
    },
    content = content,
  )
}
