import ext.AndroidAppExtension
import ext.AppExtension

plugins {
  id("com.android.application")
  id("convention.app-common")
}

val androidApp = the<AppExtension>().extensions.create<AndroidAppExtension>("android").apply {
}

android {
  namespace = "${rootProject.group}.${rootProject.name}.${project.name}"
  compileSdk = 33
  defaultConfig {
    minSdk = 26
    targetSdk = 33
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

kotlin {
  android()
}
