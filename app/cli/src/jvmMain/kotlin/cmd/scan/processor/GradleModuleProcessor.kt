package dev.petuska.kamp.cli.cmd.scan.processor

import dev.petuska.kamp.cli.cmd.scan.domain.GradleModule
import dev.petuska.kamp.core.domain.KotlinTarget

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
          } ?: KotlinTarget.Native.values()

          else -> null
        } ?: (attrs.orgJetbrainsKotlinNativeTarget ?: attrs.orgJetbrainsKotlinPlatformType)?.let(KotlinTarget::Unknown)
          ?.let(::listOf)
      }
    }?.flatten()?.toSet()
}
