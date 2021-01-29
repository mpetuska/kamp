plugins {
  kotlin("js")
  kotlin("plugin.serialization")
}

val jsOutputFile = "kamp-$version.js"
kotlin {
  js {
    binaries.executable()
    useCommonJs()
    browser {
      commonWebpackConfig {
        outputFileName = jsOutputFile
        devServer = devServer?.copy(
          port = 3000,
          proxy = mapOf("/api" to "http://localhost:8080")
        )
      }
    }
  }
  
  sourceSets {
    main {
      dependencies {
        implementation(project(rootProject.path))
        implementation("io.ktor:ktor-client-js:${Version.ktor}")
        implementation("dev.fritz2:core:${Version.fritz2}")
        implementation("dev.fritz2:components:${Version.fritz2}")
      }
    }
  }
}

tasks {
  named("processResources", Copy::class) {
    eachFile {
      if (name == "index.html") {
        expand(project.properties + mapOf("jsOutputFileName" to jsOutputFile))
      }
    }
  }
}
