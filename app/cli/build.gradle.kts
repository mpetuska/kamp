plugins {
  id("convention.app-mpp")
}

mppApp {
  jvmMainClass by "dev.petuska.kamp.cli.MainKt"
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":lib:core"))
        implementation(project(":lib:repository"))

        implementation("io.ktor:ktor-client-core:_")
        implementation("io.ktor:ktor-client-auth:_")
        implementation("io.ktor:ktor-client-content-negotiation:_")
        implementation("io.ktor:ktor-serialization-kotlinx-json:_")
        implementation("io.ktor:ktor-serialization-kotlinx-cbor:_")

        implementation("org.kodein.di:kodein-di:_")
        implementation("com.github.ajalt.clikt:clikt:_")
      }
    }
    jvmMain {
      dependencies {
        implementation("io.ktor:ktor-client-cio:_")
        implementation("org.jsoup:jsoup:_")
      }
    }
    all {
      languageSettings.apply {
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
        optIn("kotlin.time.ExperimentalTime")
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      }
    }
  }
}
