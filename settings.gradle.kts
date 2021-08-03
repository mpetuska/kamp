pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
  plugins {
    id("de.fayard.refreshVersions") version "0.11.0"
  }
}

plugins {
  id("de.fayard.refreshVersions")
}

enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "kamp"
include(
  ":shared",
  ":scanner",
  ":app:common",
  ":app:server",
  ":app:client",
  ":app:client:mdc",
)
