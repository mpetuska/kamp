plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("plugin.common")
  id("dev.petuska.klip")
}

java {
  sourceCompatibility = JavaVersion.toVersion("${project.properties["org.gradle.project.targetCompatibility"]}")
  targetCompatibility = JavaVersion.toVersion("${project.properties["org.gradle.project.targetCompatibility"]}")
}

kotlin {
  sourceSets {
    test {
      dependencies {
        implementation("dev.petuska:klip:_")
        implementation("io.kotest:kotest-runner-junit5:_")
        implementation("io.kotest:kotest-framework-engine:_")
        implementation("io.kotest:kotest-assertions-core:_")
        implementation("io.kotest:kotest-property:_")
      }
    }
  }
}
