import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.diffplug.spotless")
  idea
}

repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  maven("https://oss.sonatype.org/content/repositories/snapshots")
}

spotless {
  kotlin {
    ktfmt("_")
  }
  kotlinGradle {
    ktfmt("_")
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

tasks {
  withType<Test> {
    useJUnitPlatform()
    filter {
      isFailOnNoMatchingTests = false
    }
    testLogging {
      showExceptions = true
      showStandardStreams = true
      events = setOf(
        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
      )
      exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
  }
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "${project.properties["org.gradle.project.targetCompatibility"]}"
    }
  }
}
