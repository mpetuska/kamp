import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
  id("convention.library-jvm")
  id("convention.library-android")
  id("convention.compose")
}

android {
  namespace = "${rootProject.group}.${rootProject.name}.ui"
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(projects.lib.libClient)
        api(compose.material3)
      }
    }
    commonTest {
      dependencies {
        @OptIn(ExperimentalComposeLibrary::class)
        implementation(compose.uiTestJUnit4)
      }
    }
  }
}
