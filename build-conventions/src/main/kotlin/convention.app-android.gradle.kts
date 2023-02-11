import ext.AndroidAppExtension
import ext.AppExtension

plugins {
  id("convention.app-common")
  id("com.android.application")
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
  sourceSets {
    androidUnitTest {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
    androidInstrumentedTest {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation("androidx.test.ext:junit:_")
        implementation("androidx.test.ext:junit-ktx:_")
        implementation("androidx.test.espresso:espresso-core:_")
      }
    }
  }
}
