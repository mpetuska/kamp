plugins {
  id("com.android.library")
  id("convention.library-common")
}

android {
  compileSdk = 33
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  defaultConfig {
    minSdk = 21
    targetSdk = 33
    publishing {
      multipleVariants {
        withSourcesJar()
        withJavadocJar()
        allVariants()
      }
    }
  }
}

kotlin {
  android()
  jvmToolchain(11)
}
