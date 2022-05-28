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
        implementation("io.ktor:ktor-serialization:_")
        implementation("io.ktor:ktor-auth:_")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:_")
        implementation("ch.qos.logback:logback-classic:_")
        implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:_")
        implementation("com.microsoft.azure:applicationinsights-web-auto:_")
        implementation("org.litote.kmongo:kmongo-coroutine-serialization:_")
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
