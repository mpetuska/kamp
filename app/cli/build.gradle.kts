plugins {
  id("convention.app-mpp")
}

mppApp {
  jvmMainClass by "dev.petuska.kamp.cli.MainKt"
}

kotlin {
  sourceSets {
    jvmMain {
      dependencies {
        implementation(project(":lib:core"))
        implementation(kotlin("reflect"))

        implementation("io.ktor:ktor-client-cio:_")
        implementation("io.ktor:ktor-client-auth:_")
        implementation("io.ktor:ktor-client-content-negotiation:_")
        implementation("io.ktor:ktor-serialization-kotlinx-json:_")
        implementation("io.ktor:ktor-serialization-kotlinx-cbor:_")

        implementation("org.kodein.di:kodein-di:_")
        implementation("org.jsoup:jsoup:_")
        implementation("ch.qos.logback:logback-classic:_")
        implementation("org.jetbrains.kotlinx:kotlinx-cli:_")
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
