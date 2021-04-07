import de.fayard.refreshVersions.bootstrapRefreshVersions

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
include(":scanner", ":app")
