pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
  plugins {
    id("de.fayard.refreshVersions") version "0.23.0"
  }
}

plugins {
  id("de.fayard.refreshVersions")
}

refreshVersions {
  extraArtifactVersionKeyRules(file("versions.rules"))
}

rootProject.name = "kamp"
include(
  ":lib:core",
  ":lib:kmdc",
  ":lib:fullstack",
  ":app:cli",
  ":app:server",
  ":app:client",
)
