plugins {
  id("convention.library-jvm")
  id("convention.library-android")
  id("convention.library-js")
  id("convention.compose")
}

android {
  namespace = "${rootProject.group}.${rootProject.name}.client"
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(projects.lib.libCore)
        api(compose.runtime)
        api(libs.kotlinx.coroutines.core)

        api(libs.redux.threadsafe)
        api(libs.redux.compose)
        api(libs.redux.thunk)

        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.auth)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.serialization.kotlinx.cbor)
      }
    }
    androidMain {
      dependencies {
        api(libs.androidx.lifecycle.compose)
        implementation(libs.ktor.client.cio)
      }
    }
    jvmMain {
      dependencies {
        implementation(libs.ktor.client.cio)
      }
    }
    jsMain {
      dependencies {
        implementation(libs.ktor.client.js)
      }
    }
  }
}
