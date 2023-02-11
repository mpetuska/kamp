import de.fayard.refreshVersions.core.versionFor

plugins {
  id("convention.app-compose")
}

app {
  jvm {
    mainClass.set("dev.petuska.kodex.client.MainKt")
  }
  js {
    devServer {
      proxy = mutableMapOf("/api/*" to "https://kodex.azurewebsites.net")
    }
  }
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":lib:core"))
        implementation(project(":lib:fullstack"))
        implementation("org.reduxkotlin:redux-kotlin-threadsafe:_")
        implementation("org.reduxkotlin:redux-kotlin-thunk:_")
        implementation("org.reduxkotlin:redux-kotlin-compose:_")

        implementation("io.ktor:ktor-client-auth:_")
        implementation("io.ktor:ktor-client-content-negotiation:_")
        implementation("io.ktor:ktor-serialization-kotlinx-json:_")
        implementation("io.ktor:ktor-serialization-kotlinx-cbor:_")

        implementation("org.kodein.di:kodein-di-framework-compose:_")
      }
    }
    jsMain {
      dependencies {
        implementation("app.softwork:routing-compose:_")
        implementation("dev.petuska:kmdc:_")
        implementation(
          npm("@fortawesome/fontawesome-svg-core", versionFor("version.npm.fontawesome.core"))
        )
        implementation(
          npm("@fortawesome/free-solid-svg-icons", versionFor("version.npm.fontawesome"))
        )
        implementation(
          npm("@fortawesome/free-regular-svg-icons", versionFor("version.npm.fontawesome"))
        )
        implementation(
          npm("@fortawesome/free-brands-svg-icons", versionFor("version.npm.fontawesome"))
        )
        implementation(npm("@fortawesome/fontawesome-free", versionFor("version.npm.fontawesome")))
      }
    }
  }
}
