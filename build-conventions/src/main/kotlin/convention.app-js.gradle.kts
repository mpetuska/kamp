import ext.AppExtension
import ext.JsAppExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer

plugins {
  id("convention.app-common")
}

val app = the<AppExtension>().extensions.create<JsAppExtension>("js").apply {
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
    useCommonJs()
    browser {
      @OptIn(ExperimentalDistributionDsl::class)
      distribution {
        directory = app.distributionDir.get()
      }
      commonWebpackConfig {
        outputFileName = app.outputFileName.get()
        devServer = (devServer ?: DevServer()).apply(app.devServer.get()::execute)
        configDirectory = rootDir.resolve("gradle/webpack.config.d")
        cssSupport { enabled.set(true) }
        scssSupport { enabled.set(true) }
      }
      runTask {
        outputFileName = app.outputFileName.get()
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
            "jsOutputFileName" to app.outputFileName.get(),
            "outputFileName" to app.outputFileName.get(),
          )
        )
      }
    }
  }
  matching { it.name.startsWith("js") && it.name.endsWith("Run") }.configureEach {
    group = "run"
  }
}
