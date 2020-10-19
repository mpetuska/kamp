package scanner.processor

import kamp.domain.*
import scanner.domain.*

object GradleModuleProcessor {
  val kotlinVersion: String = "1.4.10"
  
  fun isRootModule(module: GradleModule) = module.component?.url == null
  
  fun listSupportedTargets(module: GradleModule): Set<KotlinTarget> = module.variants?.mapNotNull { variant ->
    variant.attributes?.let { attrs ->
      when (attrs.orgJetbrainsKotlinPlatformType) {
        "common" -> KotlinTarget.Common()
        "androidJvm" -> KotlinTarget.JVM.Android()
        "jvm" -> KotlinTarget.JVM.Java()
        "js" -> when (attrs.orgJetbrainsKotlinJsCompiler) {
          "legacy" -> KotlinTarget.JS.Legacy()
          "ir" -> KotlinTarget.JS.IR()
          else -> null
        }
        "native" -> attrs.orgJetbrainsKotlinNativeTarget?.let { KotlinTarget.Native(it) }
        else -> null
      }
    }
  }?.toSet() ?: setOf()
}
