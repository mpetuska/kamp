import ext.AppExtension
import ext.JsAppExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer

plugins {
  id("convention.app-common")
  id("convention.library-js")
}

val jsApp = the<AppExtension>().extensions.create<JsAppExtension>("js").apply {
  outputFileName.convention("${project.name}-${project.version}.js")
  distributionDir.convention(buildDir.resolve("dist/js/WEB-INF"))
  devServer.convention(Action {})
  devServer {
    port = 3000
    proxy = mutableMapOf("/api/*" to "http://localhost:8080")
    open = false
  }
}

kotlin {
  js(IR) {
    binaries.executable()
    browser {
      @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl::class)
      distribution {
        directory = jsApp.distributionDir.get()
      }
      commonWebpackConfig {
        outputFileName = jsApp.outputFileName.get()
        devServer = (devServer ?: DevServer()).apply(jsApp.devServer.get()::execute)
      }
      runTask {
        this.outputFileName = jsApp.outputFileName.get()
      }
    }
  }
}

tasks {
  named("jsProcessResources", Copy::class) {
    eachFile {
      if (name == "index.html") {
        expand(
          project.properties + mapOf(
            "jsOutputFileName" to jsApp.outputFileName.get(),
            "outputFileName" to jsApp.outputFileName.get(),
          )
        )
      }
    }
  }
}

afterEvaluate {
  tasks {
    names.filter { it.startsWith("js") && it.endsWith("Run") }.forEach {
      named(it) {
        group = "run"
      }
    }
  }
}
