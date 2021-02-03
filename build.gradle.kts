import org.jetbrains.kotlin.gradle.tasks.*

plugins {
  kotlin("multiplatform") version Version.kotlin
  kotlin("plugin.serialization") version Version.kotlin
  idea
}

allprojects {
  group = "lt.petuska"
  version = "0.0.1"
  apply(plugin = "idea")
  idea {
    module {
      isDownloadSources = true
      isDownloadJavadoc = true
    }
  }
  repositories {
    jcenter()
    mavenCentral()
    maven("https://dl.bintray.com/patternfly-kotlin/patternfly-fritz2")
    mavenLocal()
  }
  tasks {
    withType<Test> {
      useJUnitPlatform()
    }
    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = "15"
    }
  }
}

kotlin {
  explicitApi()
  jvm()
  js {
    browser()
  }
  
  sourceSets {
    named("commonMain") {
      dependencies {
        api("io.ktor:ktor-client-serialization:${Version.ktor}")
      }
    }
  }
}
