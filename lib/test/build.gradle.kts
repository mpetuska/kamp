plugins {
  id("convention.library-mpp")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(kotlin("test-common"))
        api(kotlin("test-annotations-common"))
        api("io.kotest:kotest-assertions-core:_")
        api("io.kotest:kotest-property:_")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
        api("dev.petuska:klip")
      }
    }
    jsMain {
      dependencies {
        api(kotlin("test-js"))
      }
    }
    jvmCommonMain {
      dependencies {
        api(kotlin("test-junit"))
      }
    }
  }
}
