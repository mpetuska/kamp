plugins {
  id("convention.app-mpp")
}

mppApp {
  jvmMainClass by "dev.petuska.kamp.server.MainKt"
}

kotlin {
  sourceSets {
    jvmMain {
      dependencies {
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

        implementation("ch.qos.logback:logback-classic:_")
        implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:_")
        implementation("org.litote.kmongo:kmongo-coroutine-serialization:_")
        implementation("com.microsoft.azure:applicationinsights-web-auto:_")
      }
    }
    all {
      languageSettings {
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }
  }
}

tasks {
  val jsBrowserDistribution = findByPath(":app:client:jsBrowserDistribution")!!
  named<Jar>("jvmJar") {
    dependsOn(jsBrowserDistribution)
    into("WEB-INF") { from(jsBrowserDistribution) }
    inputs.files(jsBrowserDistribution)
  }
  named<JavaExec>("jvmRun") {
    classpath(jsBrowserDistribution)
    systemProperty("io.ktor.development", "true")
  }
}
