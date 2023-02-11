plugins {
  id("convention.library-js")
  id("convention.library-jvm")
  id("convention.library-android")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":lib:core"))
      }
    }
    jvmMain {
      dependencies {
        implementation("org.litote.kmongo:kmongo-coroutine-serialization:_")
      }
    }
  }
}
