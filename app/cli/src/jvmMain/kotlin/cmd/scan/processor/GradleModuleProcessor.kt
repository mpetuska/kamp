package dev.petuska.kamp.cli.cmd.scan.processor

import dev.petuska.kamp.cli.cmd.scan.domain.GradleModule
import dev.petuska.kamp.core.domain.KotlinTarget

class GradleModuleProcessor {
  val GradleModule.isRootModule
    get() = component?.url == null

  val GradleModule.supportedTargets
    get(): Set<KotlinTarget>? =
      variants?.mapNotNull { variant ->
        variant.attributes?.let { attrs ->
          when (attrs.orgJetbrainsKotlinPlatformType) {
            "common" -> KotlinTarget.Common
            "androidJvm" -> KotlinTarget.JVM.Android
            "jvm" -> KotlinTarget.JVM.Java
            "js" -> when (attrs.orgJetbrainsKotlinJsCompiler) {
              "legacy" -> KotlinTarget.JS.Legacy
              "ir" -> KotlinTarget.JS.IR
              else -> null
            }

            "native" -> KotlinTarget.Native.values().find { it.id == attrs.orgJetbrainsKotlinNativeTarget }

            else -> null
          } ?: (attrs.orgJetbrainsKotlinNativeTarget ?: attrs.orgJetbrainsKotlinPlatformType)
            ?.let(KotlinTarget::Unknown)
        }
      }?.toSet()
}
