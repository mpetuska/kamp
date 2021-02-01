plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

val mainClassName = "app.IndexKt"

val jsOutputFile = "kamp-$version.js"
kotlin {
  jvm {}
  js {
    binaries.executable()
    useCommonJs()
    browser {
      distribution {
        directory = buildDir.resolve("dist/WEB-INF")
      }
      commonWebpackConfig {
        outputFileName = jsOutputFile
        devServer = devServer?.copy(
          port = 3000,
          proxy = mapOf("/api/*" to "http://localhost:8080")
        )
      }
    }
  }
  sourceSets {
    named("jvmMain") {
      dependencies {
        implementation(project(rootProject.path))
        implementation("io.ktor:ktor-server-cio:${Version.ktor}")
        implementation("org.slf4j:slf4j-simple:1.7.30")
        implementation("com.microsoft.azure:applicationinsights-web-auto:${Version.applicationInsights}")
//        implementation("ch.qos.logback:logback-classic:1.2.3")
//        implementation("org.litote.kmongo:kmongo-coroutine-serialization:${Version.kmongo}")
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
        implementation("io.ktor:ktor-client-js:${Version.ktor}")
        implementation("dev.fritz2:core:${Version.fritz2}")
        implementation("dev.fritz2:components:${Version.fritz2}")
      }
    }
  }
}

afterEvaluate {
  tasks {
    named("jsProcessResources", Copy::class) {
      eachFile {
        if (name == "index.html") {
          expand(project.properties + mapOf("jsOutputFileName" to jsOutputFile))
        }
      }
    }
    val jsBrowserDistribution by getting
    val compileKotlinJvm by getting
    val jvmProcessResources by getting
    create<JavaExec>("jvmRun") {
      group = "run"
      main = mainClassName
      dependsOn(compileKotlinJvm, jvmProcessResources)
      classpath = files(
        configurations["jvmRuntimeClasspath"],
        compileKotlinJvm.outputs,
        jvmProcessResources.outputs,
        buildDir.resolve("dist")
      )
    }
    named("jvmJar", Jar::class) {
      dependsOn(jsBrowserDistribution)
      into("WEB-INF") {
        from(jsBrowserDistribution)
      }
      val classpath =
        configurations.getByName("jvmCompileClasspath").map { if (it.isDirectory) it else zipTree(it) }
      from(classpath) {
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
      }
      
      manifest {
        attributes(
          "Main-Class" to mainClassName,
          "Implementation-Version" to project.version
        )
      }
      
      inputs.property("mainClassName", mainClassName)
      inputs.files(classpath)
      inputs.files(jsBrowserDistribution.outputs)
    }
  }
}
