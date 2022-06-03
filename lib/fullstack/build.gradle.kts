plugins {
  id("convention.mpp")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":lib:core"))
        api("org.kodein.di:kodein-di:_")
      }
    }
  }
}
