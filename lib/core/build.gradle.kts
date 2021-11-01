plugins {
  local("app-mpp")
  kotlin("plugin.serialization")
}

kotlin {
  jvm()
  js { nodejs() }

  sourceSets {
    commonMain {
      dependencies {
        api("org.kodein.di:kodein-di:_")
        api("org.jetbrains.kotlinx:kotlinx-serialization-core:_")
      }
    }
    commonTest {
      dependencies { implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_") }
    }
    named("jvmMain") { dependencies { api(kotlin("reflect")) } }
  }
}
