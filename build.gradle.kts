import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("com.github.jakemarsden.git-hooks")
  id("org.jlleitschuh.gradle.ktlint")
  idea
}

gitHooks {
  setHooks(
    mapOf(
      "post-checkout" to "ktlintApplyToIdea",
      "pre-commit" to "ktlintFormat",
      "pre-push" to "ktlintCheck"
    )
  )
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

allprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

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
      kotlinOptions {
        useIR = true
        jvmTarget = "${JavaVersion.VERSION_11}"
      }
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
  }
}
