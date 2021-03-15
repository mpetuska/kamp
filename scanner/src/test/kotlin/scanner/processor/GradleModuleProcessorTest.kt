package scanner.processor

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import io.kotest.matchers.collections.*
import kamp.domain.*
import scanner.domain.*
import scanner.testutil.*

class GradleModuleProcessorTest : FunSpec({
  val module = parseJsonFile<GradleModule>("presenter-middleware-0.2.10.module")
  
  test("isRootModule") {
    with(GradleModuleProcessor()) {
      module.isRootModule shouldBe true
      module.copy(component = module.component?.copy(url = "http")).isRootModule shouldBe false
    }
  }
  
  test("listSupportedTargets") {
    with(GradleModuleProcessor()) {
  
      val targets = module.supportedTargets
  
      targets shouldContainExactlyInAnyOrder setOf(
        KotlinTarget.JVM.Android(),
        KotlinTarget.JVM.Java(),
        KotlinTarget.JVM.Java(),
        KotlinTarget.Native("ios_arm64"),
        KotlinTarget.Native("ios_arm64"),
        KotlinTarget.Native("ios_x64"),
        KotlinTarget.Common(),
      )
  
      val targets1 = parseJsonFile<GradleModule>("redux-kotlin-0.5.5.module").supportedTargets
      targets1 shouldContainExactlyInAnyOrder setOf(
        KotlinTarget.Native("android_arm32"),
        KotlinTarget.Native("android_arm64"),
        KotlinTarget.Native("ios_arm32"),
        KotlinTarget.Native("ios_arm64"),
        KotlinTarget.Native("ios_x64"),
        KotlinTarget.Native("watchos_x86"),
        KotlinTarget.Native("watchos_arm64"),
        KotlinTarget.Native("watchos_arm32"),
        KotlinTarget.Native("wasm32"),
        KotlinTarget.Native("tvos_x64"),
        KotlinTarget.Native("tvos_arm64"),
        KotlinTarget.Native("mingw_x86"),
        KotlinTarget.Native("mingw_x64"),
        KotlinTarget.Native("macos_x64"),
        KotlinTarget.Native("linux_x64"),
        KotlinTarget.Native("linux_mipsel32"),
        KotlinTarget.Native("linux_mips32"),
        KotlinTarget.Native("linux_arm64"),
        KotlinTarget.Native("linux_arm32_hfp"),
        KotlinTarget.Common(),
        KotlinTarget.JVM.Java(),
        KotlinTarget.JS.IR(),
        KotlinTarget.JS.Legacy(),
      )
    }
  }
})
