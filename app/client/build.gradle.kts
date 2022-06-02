import de.fayard.refreshVersions.core.versionFor

plugins {
  id("convention.app-compose")
}

mppApp {
  jvmMainClass by "dev.petuska.kamp.client.MainKt"
}

val jsOutputFile = "kamp-$version.js"

kotlin {
  js {
    browser {
      distribution {
        directory = buildDir.resolve("dist/js/WEB-INF")
      }
      commonWebpackConfig {
        outputFileName = jsOutputFile
        devServer = devServer?.copy(
          port = 3000,
//          proxy = mutableMapOf("/api/*" to "http://localhost:8080"),
          proxy = mutableMapOf("/api/*" to "https://kamp.azurewebsites.net"),
          open = false
        )
      }
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":lib:fullstack"))
        implementation("org.reduxkotlin:redux-kotlin-threadsafe:_")
        implementation("org.reduxkotlin:redux-kotlin-thunk:_")

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
        implementation(npm("@fortawesome/fontawesome-svg-core", versionFor("version.npm.fontawesome.core")))
        implementation(npm("@fortawesome/free-solid-svg-icons", versionFor("version.npm.fontawesome")))
        implementation(npm("@fortawesome/free-regular-svg-icons", versionFor("version.npm.fontawesome")))
        implementation(npm("@fortawesome/free-brands-svg-icons", versionFor("version.npm.fontawesome")))
        implementation(npm("@fortawesome/fontawesome-free", versionFor("version.npm.fontawesome")))
      }
    }
    all {
      languageSettings {
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }
  }
}

tasks {
  named("jsProcessResources", Copy::class) {
    eachFile {
      if (name == "index.html") {
        expand(project.properties + mapOf("jsOutputFileName" to jsOutputFile))
      }
    }
  }
}
