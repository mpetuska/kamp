plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("com.github.johnrengelman.shadow")
  application
}

val jsModuleName = "kamp-$version"
kotlin {
  jvm {
    withJava()
  }
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
    named("jvmMain") {
      dependencies {
        implementation(project(rootProject.path))
        implementation("io.ktor:ktor-server-cio:1.4.1")
        implementation("ch.qos.logback:logback-classic:1.2.3")
        implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.2.3")
      }
    }
    named("jvmTest") {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:4.3.0")
      }
    }
  
    named("jsMain") {
      dependencies {
        implementation(project(rootProject.path))
        implementation("io.ktor:ktor-client-js:1.4.1")
        implementation("dev.fritz2:core:0.8")
      }
    }
  }
}

application {
  mainClassName = "app.IndexKt"
}

tasks {
  named("jsProcessResources", Copy::class) {
    eachFile {
      if (name == "index.html") {
        expand(project.properties + mapOf("jsOutputFileName" to "${project.name}.js"))
      }
    }
  }
  val jsBrowserDistribution by getting
  named("shadowJar", Jar::class) {
    dependsOn(jsBrowserDistribution)
    into("WEB-INF") {
      from(jsBrowserDistribution)
    }
  }
}
