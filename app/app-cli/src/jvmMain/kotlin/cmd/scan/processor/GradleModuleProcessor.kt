package dev.petuska.kodex.cli.cmd.scan.processor

import dev.petuska.kodex.cli.cmd.scan.domain.GradleModule
import dev.petuska.kodex.core.domain.KotlinTarget

class GradleModuleProcessor {
  val GradleModule.isRootModule
    get() = component?.url == null

  val GradleModule.supportedTargets
    get(): Set<KotlinTarget>? = variants?.mapNotNull { variant ->
      variant.attributes?.let { attrs ->
        when (attrs.orgJetbrainsKotlinPlatformType) {
          "common" -> KotlinTarget.Common.let(::listOf)
          "wasm" -> KotlinTarget.Wasm.let(::listOf)
          "androidJvm" -> KotlinTarget.JVM.Android.let(::listOf)
          "jvm" -> KotlinTarget.JVM.Java.let(::listOf)
          "js" -> when (attrs.orgJetbrainsKotlinJsCompiler) {
            "ir" -> KotlinTarget.JS.IR
            "legacy" -> KotlinTarget.JS.Legacy
            else -> KotlinTarget.JS.Legacy
          }.let(::listOf)

          "native" -> attrs.orgJetbrainsKotlinNativeTarget?.let { target ->
            KotlinTarget.Native.values().find { target == it.id }?.let(::listOf)
          } ?: run {
            if (component?.module == "kotlin-test") KotlinTarget.Native.values() else null
          }

          else -> null
        } ?: (attrs.orgJetbrainsKotlinNativeTarget ?: attrs.orgJetbrainsKotlinPlatformType)?.let(
          KotlinTarget::Unknown
        )
          ?.let(::listOf)
      }
    }?.flatten()?.toSet()
}
