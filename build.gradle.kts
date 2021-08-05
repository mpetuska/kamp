import de.fayard.refreshVersions.core.versionFor

plugins {
  kotlin("multiplatform") apply false
  id("com.github.jakemarsden.git-hooks")
  id("org.jlleitschuh.gradle.ktlint")
  idea
}

gitHooks {
  setHooks(
    mapOf(
      "pre-commit" to "ktlintFormat",
      "pre-push" to "ktlintCheck"
    )
  )
}

allprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")
  apply(plugin = "idea")

  idea {
    module {
      isDownloadSources = true
      isDownloadJavadoc = true
    }
  }

  ktlint {
    version by versionFor("version.com.pinterest..ktlint")
    additionalEditorconfigFile.set(rootDir.resolve(".editorconfig"))
  }

  repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }

  tasks {
    withType<Test> {
      useJUnitPlatform()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions {
        jvmTarget = "${project.properties["org.gradle.project.targetCompatibility"]}"
      }
    }
  }
}
