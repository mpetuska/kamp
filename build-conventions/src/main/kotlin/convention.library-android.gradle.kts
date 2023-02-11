plugins {
  id("com.android.library")
  id("convention.library-common")
}

android {
  namespace = "${rootProject.group}.${rootProject.name}.${project.name}"
  compileSdk = 33
  defaultConfig {
    minSdk = 21
    targetSdk = 33
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
