import de.fayard.refreshVersions.bootstrapRefreshVersions
pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

buildscript {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
  dependencies {
    classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
  }
}

bootstrapRefreshVersions()

rootProject.name = "kamp"
include(
  ":shared",
  ":scanner",
  ":app:common",
  ":app:client",
  ":app:server",
)
