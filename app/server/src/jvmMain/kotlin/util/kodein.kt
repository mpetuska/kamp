package dev.petuska.kamp.server.util

import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import org.kodein.di.*
import org.kodein.di.bindings.NoArgBindingDI
import org.kodein.di.bindings.Provider
import org.kodein.di.ktor.closestDI

inline fun <reified T : Any> DI.Builder.callProvider(
  noinline creator: NoArgBindingDI<ApplicationCall>.() -> T
): Provider<ApplicationCall, T> = contexted<ApplicationCall>().provider(creator)

inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.inject(tag: Any? = null): LazyDelegate<T> =
  closestDI().on(context).instance<T>(tag)
