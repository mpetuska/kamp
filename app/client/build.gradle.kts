import de.fayard.refreshVersions.core.versionFor

plugins {
  local("app-mpp")
  id("org.jetbrains.compose")
}

val jsOutputFile = "kamp-$version.js"

kotlin {
  jvm()
  js {
    useCommonJs()
    binaries.executable()
    browser {
      distribution { directory = buildDir.resolve("dist/js/WEB-INF") }
      commonWebpackConfig {
        cssSupport.enabled = true
        outputFileName = jsOutputFile
        devServer =
            devServer?.copy(
                port = 3000,
                proxy = mutableMapOf("/api/*" to "http://localhost:8080"),
                // proxy = mutableMapOf("/api/*" to "https://kamp.azurewebsites.net"),
                open = false)
      }
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(compose.runtime)
        implementation(project(":lib:fullstack"))
        implementation("org.reduxkotlin:redux-kotlin-threadsafe:_")
        implementation("org.reduxkotlin:redux-kotlin-thunk:_")
        implementation("io.ktor:ktor-client-auth:_")
        implementation("io.ktor:ktor-client-serialization:_")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:_")
        implementation("org.kodein.di:kodein-di-framework-compose:_")
      }
    }
    named("jsMain") {
      dependencies {
        implementation(project(":lib:kmdc"))
        implementation("app.softwork:routing-compose:_")
        implementation(
            npm("@fortawesome/fontawesome-svg-core", versionFor("version.npm.fontawesome.core")))
        implementation(
            npm("@fortawesome/free-solid-svg-icons", versionFor("version.npm.fontawesome")))
        implementation(
            npm("@fortawesome/free-regular-svg-icons", versionFor("version.npm.fontawesome")))
        implementation(
            npm("@fortawesome/free-brands-svg-icons", versionFor("version.npm.fontawesome")))
        implementation(npm("@fortawesome/fontawesome-free", versionFor("version.npm.fontawesome")))
      }
    }
    named("jvmMain") { dependencies {} }
    all { languageSettings { optIn("kotlinx.serialization.ExperimentalSerializationApi") } }
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

// Workaround for https://kotlinlang.slack.com/archives/C0B8L3U69/p1633590092096600
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
  rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().apply {
    resolution("@webpack-cli/serve", "1.5.2")
  }
}
