package scanner.processor

import kamp.domain.*
import scanner.domain.*

class GradleModuleProcessor {
  val kotlinVersion: String = "1.4.30"
  
  val GradleModule.isRootModule get() = component?.url == null
  
  val GradleModule.supportedTargets
    get(): Set<KotlinTarget>? = variants?.mapNotNull { variant ->
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
    }?.toSet()
}
