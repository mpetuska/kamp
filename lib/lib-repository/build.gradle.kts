plugins {
  id("convention.library-jvm")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.lib.libCore)
        implementation(libs.koin.logger.slf4j)
      }
    }
    jvmMain {
      dependencies {
        implementation(libs.kmongo)
      }
    }
  }
}
