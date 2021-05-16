plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  jvm {}
  js {
    useCommonJs()
    browser {}
  }
  sourceSets {
    commonMain {
      dependencies {
        api(rootProject.project(":shared"))
        api("org.kodein.di:kodein-di:_")
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
        api("io.ktor:ktor-client-serialization:_")
      }
    }
    named("jvmTest") {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:_")
      }
    }
  }
}
