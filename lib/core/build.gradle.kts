plugins {
  id("convention.mpp")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api("org.kodein.di:kodein-di:_")
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
      }
    }
    jvmMain {
      dependencies {
        api(kotlin("reflect"))
      }
    }
  }
}
