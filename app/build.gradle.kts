plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("com.bnorm.react.kotlin-react-function") version Version.reactFunction
}

val mainClassName = "app.IndexKt"

val jsOutputFile = "kamp-$version.js"
kotlin {
  jvm {}
  js {
    useCommonJs()
    binaries.executable()
    browser {
      distribution {
        directory = buildDir.resolve("dist/WEB-INF")
      }
      commonWebpackConfig {
        cssSupport.enabled = true
        outputFileName = jsOutputFile
        devServer = devServer?.copy(
          port = 3000,
          proxy = mapOf("/api/*" to "http://localhost:8080")
        )
      }
    }
  }
  sourceSets {
    named("commonMain") {
      dependencies {
        implementation(project(rootProject.path))
        implementation("org.kodein.di:kodein-di:${Version.kodein}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.serialization}")
      }
    }
    named("jvmMain") {
      dependencies {
        implementation("io.ktor:ktor-server-cio:${Version.ktor}")
        implementation("io.ktor:ktor-serialization:${Version.ktor}")
        implementation("ch.qos.logback:logback-classic:${Version.logback}")
        implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:${Version.kodein}")
        implementation("com.microsoft.azure:applicationinsights-web-auto:${Version.applicationInsights}")
//        implementation("org.litote.kmongo:kmongo-coroutine-serialization:${Version.kmongo}")
      }
    }
    named("jsMain") {
      dependencies {
        implementation("io.ktor:ktor-client-serialization:${Version.ktor}")
        implementation("org.jetbrains:kotlin-react:${Version.react}")
        implementation("org.jetbrains:kotlin-react-dom:${Version.react}")
        implementation("org.jetbrains:kotlin-styled:${Version.styledComponents}")
//        implementation("com.ccfraser.muirwik:muirwik-components:${Version.murwik}")
//        implementation("com.bnorm.react:kotlin-react-function:${Version.reactFunction}")
      }
    }
    named("jvmTest") {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:4.3.0")
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
          "Built-By" to System.getProperty("user.name"),
          "Build-Jdk" to System.getProperty("java.version"),
          "Implementation-Version" to project.version,
          "Created-By" to "Gradle v${org.gradle.util.GradleVersion.current()}",
          "Created-From" to Git.headCommitHash
        )
      }
      
      inputs.property("mainClassName", mainClassName)
      inputs.files(classpath)
      inputs.files(jsBrowserDistribution.outputs)
    }
  }
}
