plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("com.github.johnrengelman.shadow")
  application
}

dependencies {
  implementation("io.ktor:ktor-client-cio:1.4.1")
  implementation("io.ktor:ktor-client-serialization:1.4.1")
  implementation("org.jsoup:jsoup:1.13.1")
  implementation(project(rootProject.path))
  implementation("ch.qos.logback:logback-classic:1.2.3")
  
  testImplementation("io.kotest:kotest-runner-junit5:4.3.0")
}

application {
  mainClassName = "scanner.IndexKt"
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
