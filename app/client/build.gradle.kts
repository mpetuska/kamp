plugins {
  kotlin("js")
  kotlin("plugin.serialization")
}

val jsModuleName = "kamp-$version"
kotlin {
  js {
    binaries.executable()
    moduleName = jsModuleName
    browser {
      commonWebpackConfig {
        devServer = devServer?.copy(
          port = 3000,
          proxy = mapOf("*" to "http://localhost:8080")
        )
      }
    }
  }
  
  sourceSets {
    main {
      dependencies {
        implementation(project(rootProject.path))
        implementation("io.ktor:ktor-client-js:1.4.1")
        implementation("dev.fritz2:core:0.8")
      }
    }
  }
}

tasks {
  named("processResources", Copy::class) {
    eachFile {
      if (name == "index.html") {
        expand(project.properties + mapOf("jsOutputFileName" to "${project.name}.js"))
      }
    }
  }
  val browserDistribution by getting
}
