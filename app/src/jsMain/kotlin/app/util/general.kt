package app.util

import app.config.env
import app.view.KampComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.BasicComponent
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.styled
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

external fun require(module: String): dynamic

inline fun <T1, T2> suspending(crossinline block: suspend CoroutineScope.(T1, T2) -> Unit): (T1, T2) -> Unit =
  { t1, t2 -> suspending { block(t1, t2) } }

inline fun <T1> suspending(crossinline block: suspend CoroutineScope.(T1) -> Unit): (T1) -> Unit =
  { suspending { block(it) } }

inline fun suspending(crossinline block: suspend CoroutineScope.() -> Unit) {
  GlobalScope.launch { block() }
}

fun String.toApi() = "${window.env.API_URL}/${this.removePrefix("/")}"

typealias StyledComponent<E> = RenderContext.(style: BoxParams.() -> Unit, block: E.() -> Unit) -> E

@KampComponent
fun <E> styled(component: BasicComponent<E>): StyledComponent<E> = { style, block ->
  (component.styled(styling = style))(block)
}
