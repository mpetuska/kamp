package dev.petuska.kamp.cli.cmd.scan.processor

import dev.petuska.kamp.cli.cmd.scan.domain.GradleModule
import dev.petuska.kamp.cli.testutil.parseJsonFile
import dev.petuska.kamp.core.domain.KotlinTarget
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class GradleModuleProcessorTest {
  @TestFactory
  fun tests(): Array<DynamicTest> {
    val module = parseJsonFile<GradleModule>("presenter-middleware-0.2.10.module")

    return arrayOf(
      dynamicTest("isRootModule") {
        with(GradleModuleProcessor()) {
          module.isRootModule shouldBe true
          module.copy(component = module.component?.copy(url = "http")).isRootModule shouldBe false
        }
      },
      dynamicTest("listSupportedTargets") {
        with(GradleModuleProcessor()) {
          val targets = module.supportedTargets

          targets shouldContainExactlyInAnyOrder setOf(
            KotlinTarget.JVM.Android,
            KotlinTarget.JVM.Java,
            KotlinTarget.JVM.Java,
            KotlinTarget.Native.IOS.IOSArm64,
            KotlinTarget.Native.IOS.IOSX64,
            KotlinTarget.Common,
          )

          val targets1 = parseJsonFile<GradleModule>("redux-kotlin-0.5.5.module").supportedTargets
          targets1 shouldContainExactlyInAnyOrder setOf(
            KotlinTarget.Native.AndroidNative.AndroidNativeArm32,
            KotlinTarget.Native.AndroidNative.AndroidNativeArm64,
            KotlinTarget.Native.IOS.IOSArm32,
            KotlinTarget.Native.IOS.IOSArm64,
            KotlinTarget.Native.IOS.IOSX64,
            KotlinTarget.Native.WatchOS.WatchOSArm32,
            KotlinTarget.Native.WatchOS.WatchOSArm64,
            KotlinTarget.Native.WatchOS.WatchOSX86,
            KotlinTarget.Native.Wasm32,
            KotlinTarget.Native.TvOS.TvOSArm64,
            KotlinTarget.Native.TvOS.TvOSX64,
            KotlinTarget.Native.Mingw.MingwX64,
            KotlinTarget.Native.Mingw.MingwX86,
            KotlinTarget.Native.MacOS.MacOSX64,
            KotlinTarget.Native.Linux.LinuxX64,
            KotlinTarget.Native.Linux.LinuxMipsel32,
            KotlinTarget.Native.Linux.LinuxMips32,
            KotlinTarget.Native.Linux.LinuxArm64,
            KotlinTarget.Native.Linux.LinuxArm32Hfp,
            KotlinTarget.Common,
            KotlinTarget.JVM.Java,
            KotlinTarget.JS.IR,
            KotlinTarget.JS.Legacy,
          )
        }
      }
    )
  }
}
