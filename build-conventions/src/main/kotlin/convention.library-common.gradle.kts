import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("convention.common")
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("dev.petuska.klip")
}

kotlin {
  sourceSets {
    configureEach {
      languageSettings {
        optIn("kotlin.RequiresOptIn")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }
    commonMain {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:_")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(project(":lib:test"))
      }
    }
  }
}

tasks {
  withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
  }
}
