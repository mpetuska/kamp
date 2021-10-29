plugins {
  local("app-jvm")
  kotlin("plugin.serialization")
}

kotlin {
  dependencies {
    implementation(project(":lib:core"))
    implementation(kotlin("reflect"))
    implementation("io.ktor:ktor-client-cio:_")
    implementation("io.ktor:ktor-client-auth:_")
    implementation("io.ktor:ktor-client-serialization:_")
    implementation("org.kodein.di:kodein-di:_")
    implementation("org.jsoup:jsoup:_")
    implementation("ch.qos.logback:logback-classic:_")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:_")

    testImplementation("io.kotest:kotest-runner-junit5:_")
  }
  sourceSets.all {
    languageSettings.apply {
      optIn("kotlinx.serialization.ExperimentalSerializationApi")
      optIn("kotlin.time.ExperimentalTime")
      optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
  }
}

val mainClassName = "dev.petuska.kamp.cli.MainKt"

tasks {
  val compileKotlin by getting
  val processResources by getting
  create<JavaExec>("run") {
    dependsOn(compileKotlin, processResources)
    group = "run"
    mainClass by mainClassName
    classpath =
        files(configurations.runtimeClasspath, compileKotlin.outputs, processResources.outputs)
  }
  jar {
    duplicatesStrategy = DuplicatesStrategy.WARN
    val classpath =
        configurations.runtimeClasspath.get().files.map { if (it.isDirectory) it else zipTree(it) }
    duplicatesStrategy = DuplicatesStrategy.WARN
    from(classpath) { exclude("META-INF/**") }

    manifest {
      attributes(
          "Main-Class" to mainClassName,
          "Built-By" to System.getProperty("user.name"),
          "Build-Jdk" to System.getProperty("java.version"),
          "Implementation-Version" to project.version,
          "Created-By" to "Gradle v${GradleVersion.current()}",
          "Created-From" to Git.headCommitHash)
    }

    inputs.property("mainClassName", mainClassName)
    inputs.files(classpath)
    outputs.file(archiveFile)
  }
}
