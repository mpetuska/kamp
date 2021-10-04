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
  extraArtifactVersionKeyRules(file("refreshVersions.rules"))
}

enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "kamp"
include(
  ":shared",
  ":scanner",
  ":app:common",
  ":app:server",
  ":app:client",
  ":app:client:kmdc",
)
