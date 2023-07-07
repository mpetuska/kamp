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
        @OptIn(ExperimentalComposeLibrary::class)
        api(compose.components.resources)
        api(compose.preview)
        api(compose.material3)
      }
    }
    jvmMain {
      dependencies {
        api(compose.desktop.currentOs)
      }
    }
    androidMain {
      dependencies {
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
