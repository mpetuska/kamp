plugins {
  local("app-mpp")
  kotlin("plugin.serialization")
}

kotlin {
  jvm()
  js { browser() }

  sourceSets {
    commonMain {
      dependencies {
        api("org.kodein.di:kodein-di:_")
        api("org.jetbrains.kotlinx:kotlinx-serialization-core:_")
      }
    }
    named("jvmMain") { dependencies { api(kotlin("reflect")) } }
  }
}
