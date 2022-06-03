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

            "native" -> when (attrs.orgJetbrainsKotlinNativeTarget) {
              "android_arm32" -> KotlinTarget.Native.AndroidNative.AndroidNativeArm32
              "android_arm64" -> KotlinTarget.Native.AndroidNative.AndroidNativeArm64
              "ios_arm32" -> KotlinTarget.Native.IOS.IOSArm32
              "ios_arm64" -> KotlinTarget.Native.IOS.IOSArm64
              "ios_simulator_arm64" -> KotlinTarget.Native.IOS.IOSSimulatorArm64
              "ios_x64" -> KotlinTarget.Native.IOS.IOSX64
              "linux_arm32_hfp" -> KotlinTarget.Native.Linux.LinuxArm32Hfp
              "linux_arm64" -> KotlinTarget.Native.Linux.LinuxArm64
              "linux_mips32" -> KotlinTarget.Native.Linux.LinuxMips32
              "linux_mipsel32" -> KotlinTarget.Native.Linux.LinuxMipsel32
              "linux_x64" -> KotlinTarget.Native.Linux.LinuxX64
              "macos_arm64" -> KotlinTarget.Native.MacOS.MacOSArm64
              "macos_x64" -> KotlinTarget.Native.MacOS.MacOSX64
              "tvos_arm64" -> KotlinTarget.Native.TvOS.TvOSArm64
              "tvos_simulator_arm64" -> KotlinTarget.Native.TvOS.TvOSSimulatorArm64
              "tvos_x64" -> KotlinTarget.Native.TvOS.TvOSX64
              "watchos_arm32" -> KotlinTarget.Native.WatchOS.WatchOSArm32
              "watchos_arm64" -> KotlinTarget.Native.WatchOS.WatchOSArm64
              "watchos_simulator_arm64" -> KotlinTarget.Native.WatchOS.WatchOSSimulatorArm64
              "watchos_x64" -> KotlinTarget.Native.WatchOS.WatchOSX64
              "watchos_x86" -> KotlinTarget.Native.WatchOS.WatchOSX86
              "mingw_x64" -> KotlinTarget.Native.Mingw.MingwX64
              "mingw_x86" -> KotlinTarget.Native.Mingw.MingwX86
              "wasm32" -> KotlinTarget.Native.Wasm32
              else -> null
            }

            else -> null
          } ?: (attrs.orgJetbrainsKotlinNativeTarget ?: attrs.orgJetbrainsKotlinPlatformType)
            ?.let(KotlinTarget::Unknown)
        }
      }?.toSet()
}
