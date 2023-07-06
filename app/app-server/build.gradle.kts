import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("convention.app-jvm")
}

app {
  jvm {
    mainClass.set("dev.petuska.kodex.server.MainKt")
  }
}

kotlin {
  sourceSets {
    jvmMain {
      dependencies {
        implementation(projects.lib.libCore)
        implementation(projects.lib.libRepository)

        implementation(libs.ktor.server.cio)
        implementation(libs.ktor.server.auth)
        implementation(libs.ktor.server.call.logging)
        implementation(libs.ktor.server.default.headers)
        implementation(libs.ktor.server.caching.headers)
        implementation(libs.ktor.server.status.pages)
        implementation(libs.ktor.server.compression)
        implementation(libs.ktor.server.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.serialization.kotlinx.cbor)

        implementation(libs.koin.ktor)
        implementation(libs.koin.logger.slf4j)
        implementation(libs.logback.classic)
      }
    }
  }
}

tasks {
  val jsBrowserDistribution = findByPath(":app:app-client:app-client-web:jsBrowserDistribution")
  named<ShadowJar>("jvmFatJar") {
    dependsOn(jsBrowserDistribution)
    into("WEB-INF") {
      from(jsBrowserDistribution)
      exclude("**/*.scss")
    }
    inputs.files(jsBrowserDistribution)
  }
  named<JavaExec>("jvmRun") {
    classpath(project(":app:app-client:app-client-web").buildDir.resolve("dist"))
    systemProperty("io.ktor.development", "true")
  }
}
