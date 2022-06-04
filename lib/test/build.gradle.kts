plugins {
  id("convention.mpp")
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
      }
    }
    jsMain {
      dependencies {
        api(kotlin("test-js"))
      }
    }
    jvmMain {
      dependencies {
        api(kotlin("test-junit5"))
      }
    }
  }
}
