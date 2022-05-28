plugins {
  id("convention.mpp")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":lib:core"))
        api("org.kodein.di:kodein-di:_")
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
        api("io.ktor:ktor-client-serialization:_")
      }
    }
  }
}
