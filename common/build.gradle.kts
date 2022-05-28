import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("org.jlleitschuh.gradle.ktlint")
}

repositories {
  mavenCentral()
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "${JavaVersion.VERSION_11}"
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
        api("io.ktor:ktor-client-serialization:_")
      }
    }
    named("jvmMain") {
      dependencies {
        api(kotlin("reflect"))
      }
    }
    all {
      languageSettings.apply {
        useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
      }
    }
  }
}
