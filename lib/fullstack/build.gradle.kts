plugins { local("app-mpp") }

kotlin {
  jvm {}
  js {
    useCommonJs()
    browser {}
  }
  sourceSets {
    commonMain {
      dependencies {
        api(project(":lib:core"))
        api("org.kodein.di:kodein-di:_")
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
        api("io.ktor:ktor-client-serialization:_")
      }
    }
    named("jvmTest") { dependencies { implementation("io.kotest:kotest-runner-junit5:_") } }
  }
}
