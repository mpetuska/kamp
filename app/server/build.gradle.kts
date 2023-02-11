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
        implementation(project(":lib:core"))
        implementation(project(":lib:repository"))
        implementation(project(":lib:fullstack"))

        implementation("io.ktor:ktor-server-cio:_")
        implementation("io.ktor:ktor-server-auth:_")
        implementation("io.ktor:ktor-server-call-logging:_")
        implementation("io.ktor:ktor-server-default-headers:_")
        implementation("io.ktor:ktor-server-caching-headers:_")
        implementation("io.ktor:ktor-server-status-pages:_")
        implementation("io.ktor:ktor-server-compression:_")
        implementation("io.ktor:ktor-server-content-negotiation:_")
        implementation("io.ktor:ktor-serialization-kotlinx-json:_")
        implementation("io.ktor:ktor-serialization-kotlinx-cbor:_")

        implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:_")
      }
    }
  }
}

tasks {
  val jsBrowserDistribution = findByPath(":app:client:jsBrowserDistribution")
  named<ShadowJar>("jvmFatJar") {
    dependsOn(jsBrowserDistribution)
    into("WEB-INF") {
      from(jsBrowserDistribution)
      exclude("**/*.scss")
    }
    inputs.files(jsBrowserDistribution)
  }
  named<JavaExec>("jvmRun") {
    classpath(project(":app:client").buildDir.resolve("dist"))
    systemProperty("io.ktor.development", "true")
  }
}
