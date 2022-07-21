plugins {
  id("convention.mpp")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":lib:core"))
      }
    }
  }
}
