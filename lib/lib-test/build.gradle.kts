plugins {
  id("convention.library-js")
  id("convention.library-jvm")
  id("convention.library-android")
}

android {
  namespace = "${rootProject.group}.${rootProject.name}.test"
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(libs.kotlin.test.common)
        api(libs.kotlin.test.annotations.common)
        api(libs.kotest.assertions.core)
        api(libs.kotlinx.coroutines.test)
      }
    }
    jvmMain {
      dependencies {
        api(libs.kotlin.test.junit)
      }
    }
    androidMain {
      dependencies {
        api(libs.kotlin.test.junit)
      }
    }
    jsMain {
      dependencies {
        api(libs.kotlin.test.js)
      }
    }
  }
}
