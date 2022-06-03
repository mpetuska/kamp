plugins {
  id("convention.mpp")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":lib:core"))
      }
    }
    jvmMain {
      dependencies {
        implementation("org.litote.kmongo:kmongo-coroutine-serialization:_")
      }
    }
  }
}
