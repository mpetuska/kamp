plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

val mainClassName = "app.server.MainKt"
kotlin {
  sourceSets {
    main {
      dependencies {
        implementation(project(":app:common"))
        implementation("io.ktor:ktor-server-cio:_")
        implementation("io.ktor:ktor-serialization:_")
        implementation("io.ktor:ktor-auth:_")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:_")
        implementation("ch.qos.logback:logback-classic:_")
        implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:_")
        implementation("com.microsoft.azure:applicationinsights-web-auto:_")
        implementation("org.litote.kmongo:kmongo-coroutine-serialization:_")
      }
    }
    test {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:_")
      }
    }
    all {
      languageSettings {
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }
  }
}

afterEvaluate {
  tasks {
    val jsBrowserDistribution = findByPath(":app:client:jsBrowserDistribution")!!
    val compileKotlin by getting
    val processResources by getting
    create<JavaExec>("run") {
      group = "run"
      mainClass by mainClassName
      dependsOn(compileKotlin, processResources)
      systemProperty("io.ktor.development", "true")
      classpath = files(
        configurations["runtimeClasspath"],
        compileKotlin.outputs,
        processResources.outputs,
        project(":app:client").buildDir.resolve("dist/js")
      )
    }
    jar {
      dependsOn(jsBrowserDistribution)
      into("WEB-INF") {
        from(jsBrowserDistribution)
      }
      val classpath = configurations.getByName("runtimeClasspath")
        .map { if (it.isDirectory) it else zipTree(it) }
      duplicatesStrategy = DuplicatesStrategy.WARN
      from(classpath) {
        exclude("META-INF/**")
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
