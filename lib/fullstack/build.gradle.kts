plugins {
  id("convention.mpp")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":lib:core"))
        api("org.kodein.di:kodein-di:_")
        api("io.ktor:ktor-serialization-kotlinx-json:_")
      }
    }
  }
}
