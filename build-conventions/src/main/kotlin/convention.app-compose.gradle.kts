plugins {
  id("convention.app-mpp")
  id("convention.compose")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(compose.runtime)
      }
    }
    jvmMain {
      dependencies {
        implementation(compose.desktop.currentOs)
      }
    }
    jsMain {
      dependencies {
        implementation(compose.web.core)
        implementation(compose.web.svg)
      }
    }
    jsTest {
      dependencies {
        implementation(compose.web.testUtils)
      }
      languageSettings {
        optIn("org.jetbrains.compose.web.testutils.ComposeWebExperimentalTestsApi")
      }
    }
  }
}
