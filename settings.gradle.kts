pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

plugins {
  id("de.fayard.refreshVersions") version "0.40.2"
  id("com.gradle.enterprise") version "3.10.3"
}

refreshVersions {
  versionsPropertiesFile = rootDir.resolve("gradle/versions.properties")
  extraArtifactVersionKeyRules(rootDir.resolve("gradle/versions.rules"))
}
includeBuild("build-conventions")

rootProject.name = "kamp"
include(
  ":lib:test",
  ":lib:core",
  ":lib:repository",
  ":lib:fullstack",
  ":app:cli",
  ":app:server",
  ":app:client",
)
