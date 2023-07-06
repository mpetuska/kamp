pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}
plugins {
  id("com.gradle.enterprise") version "3.12.3"
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
includeBuild("build-conventions")

rootProject.name = "kodex"
include(
  ":lib:lib-test",
  ":lib:lib-core",
  ":lib:lib-repository",
  ":lib:lib-client",
  ":lib:lib-ui",
)
include(
  ":app:app-cli",
  ":app:app-server",
//  ":app:app-client:app-client-common",
  ":app:app-client:app-client-web",
)
