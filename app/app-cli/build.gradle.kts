plugins {
  id("convention.app-jvm")
  alias(libs.plugins.kotlin.serialization)
}

app {
  jvm {
    mainClass.set("dev.petuska.kodex.cli.MainKt")
  }
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.lib.libCore)
        implementation(projects.lib.libRepository)

        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.auth)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.serialization.kotlinx.cbor)

        implementation(libs.clikt)
        implementation(libs.koin.core)
        implementation(libs.koin.logger.slf4j)
      }
    }
    commonTest {
      dependencies {
        implementation(projects.lib.libTest)
      }
    }
    jvmMain {
      dependencies {
        implementation(libs.ktor.client.cio)
        implementation(libs.jsoup)
        implementation(libs.logback.classic)
      }
    }
  }
}
