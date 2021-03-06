plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
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
          proxy = mapOf("/api/*" to "http://localhost:8080"),
          open = false
        )
      }
    }
  }
  sourceSets {
    named("commonMain") {
      dependencies {
        implementation(project(rootProject.path))
        implementation("org.kodein.di:kodein-di:_")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
      }
    }
    named("jvmMain") {
      dependencies {
        implementation("io.ktor:ktor-server-cio:_")
        implementation("io.ktor:ktor-serialization:_")
        implementation("io.ktor:ktor-auth:_")
        implementation("ch.qos.logback:logback-classic:_")
        implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:_")
        implementation("com.microsoft.azure:applicationinsights-web-auto:_")
        implementation("org.litote.kmongo:kmongo-coroutine-serialization:_")
      }
    }
    named("jsMain") {
      dependencies {
        implementation("io.ktor:ktor-client-serialization:_")
        implementation("dev.fritz2:core:_")
        implementation("dev.fritz2:components:_")
      }
      languageSettings.apply {
        useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
      }
    }
    named("jvmTest") {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:_")
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
        configurations.getByName("jvmRuntimeClasspath").map { if (it.isDirectory) it else zipTree(it) }
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
