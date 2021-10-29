plugins {
  id("plugin.common")
  kotlin("jvm")
  kotlin("plugin.serialization")
}

java {
  sourceCompatibility = JavaVersion.toVersion("${project.properties["org.gradle.project.targetCompatibility"]}")
  targetCompatibility = JavaVersion.toVersion("${project.properties["org.gradle.project.targetCompatibility"]}")
}

kotlin {
  sourceSets {
    test {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
  }
}
