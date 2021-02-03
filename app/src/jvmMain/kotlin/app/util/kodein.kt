package app.util

import io.ktor.application.*
import io.ktor.util.pipeline.*
import org.kodein.di.*
import org.kodein.di.bindings.*
import org.kodein.di.ktor.*

inline fun <reified T : Any> DI.Builder.callProvider(noinline creator: NoArgBindingDI<ApplicationCall>.() -> T) =
  contexted<ApplicationCall>().provider(creator)

inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.inject(tag: Any? = null) =
  di().on(context).instance<T>(tag)
