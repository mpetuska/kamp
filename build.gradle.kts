import org.jetbrains.kotlin.gradle.tasks.*

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("com.github.jakemarsden.git-hooks")
  id("com.diffplug.spotless")
  idea
}

allprojects {
  group = "lt.petuska"
  version = "0.0.1"
  apply(plugin = "idea")
  apply(plugin = "com.diffplug.spotless")
  
  spotless {
    kotlin {
      ktfmt("0.22")
    }
  }
  idea {
    module {
      isDownloadSources = true
      isDownloadJavadoc = true
    }
  }
  repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://kotlin.bintray.com/kotlinx")
  }
  tasks {
    withType<Test> {
      useJUnitPlatform()
    }
    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = "15"
      kotlinOptions {
        useIR = true
        jvmTarget = "${JavaVersion.VERSION_11}"
      }
    }
  }
}

gitHooks {
  setHooks(mapOf("pre-commit" to "spotlessApply", "pre-push" to "check"))
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
        api("io.ktor:ktor-client-serialization:_")
      }
    }
    named("jvmMain") {
      dependencies {
        api(kotlin("reflect"))
      }
    }
  }
}
