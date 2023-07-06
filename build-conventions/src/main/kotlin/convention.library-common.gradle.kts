plugins {
  id("convention.common")
  kotlin("multiplatform")
}

kotlin {
  sourceSets {
    commonTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
