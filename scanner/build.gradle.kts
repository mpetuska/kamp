plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

kotlin {
  dependencies {
    implementation(project(":shared"))
    implementation("io.ktor:ktor-client-cio:_")
    implementation("io.ktor:ktor-client-auth:_")
    implementation("io.ktor:ktor-client-serialization:_")
    implementation("org.kodein.di:kodein-di:_")
    implementation("org.jsoup:jsoup:_")
    implementation("ch.qos.logback:logback-classic:_")
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-cli:_")

    testImplementation("io.kotest:kotest-runner-junit5:_")
  }
  sourceSets.all {
    languageSettings.apply {
      useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
      useExperimentalAnnotation("io.ktor.util.KtorExperimentalAPI")
      useExperimentalAnnotation("kotlin.time.ExperimentalTime")
      useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
      useExperimentalAnnotation("kotlinx.cli.ExperimentalCli")
    }
  }
}

val mainClassName = "scanner.MainKt"

tasks {
  val compileKotlin by getting
  val processResources by getting
  create<JavaExec>("run") {
    group = "run"
    main = mainClassName
    dependsOn(compileKotlin, processResources)
    classpath = files(
      configurations.runtimeClasspath,
      compileKotlin.outputs,
      processResources.outputs
    )
  }
  jar {
    val classpath = configurations.runtimeClasspath.get().files.map { if (it.isDirectory) it else zipTree(it) }
    duplicatesStrategy = DuplicatesStrategy.WARN
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
    outputs.file(archiveFile)
  }
}
