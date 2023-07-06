plugins {
  id("convention.library-js")
  id("convention.library-jvm")
  id("convention.library-android")
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
}

android {
  namespace = "${rootProject.group}.${rootProject.name}.core"
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(libs.koin.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.kotlinx.serialization.cbor)
      }
    }
    commonTest {
      dependencies {
        implementation(projects.lib.libTest)
      }
    }
  }
}

dependencies {
  kspCommonMainMetadata(libs.koin.compiler)
}
