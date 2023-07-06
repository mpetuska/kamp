plugins {
  id("convention.app-js")
  id("convention.compose")
}

app {
  js {
    devServer {
      proxy = mutableMapOf("/api/*" to "https://kodex.azurewebsites.net")
    }
  }
}

kotlin {
  sourceSets {
    jsMain {
      dependencies {
        dependencies {
          implementation(projects.lib.libClient)
          implementation(libs.kmdc)
          implementation(libs.compose.routing)
          implementation(libs.koin.compose)
        }
      }
    }
    jsTest {
      dependencies {
        dependencies {
          implementation(compose.html.testUtils)
        }
      }
    }
  }
}
