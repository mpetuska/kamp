package dev.petuska.kodex.server.util

import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import org.kodein.di.DI
import org.kodein.di.LazyDelegate
import org.kodein.di.bindings.NoArgBindingDI
import org.kodein.di.bindings.Provider
import org.kodein.di.contexted
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.on
import org.kodein.di.provider

inline fun <reified T : Any> DI.Builder.callProvider(
  noinline creator: NoArgBindingDI<ApplicationCall>.() -> T
): Provider<ApplicationCall, T> = contexted<ApplicationCall>().provider(creator)

inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.inject(
  tag: Any? = null
): LazyDelegate<T> =
  closestDI().on(context).instance<T>(tag)
