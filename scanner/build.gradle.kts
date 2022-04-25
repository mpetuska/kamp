plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("org.jlleitschuh.gradle.ktlint")
}

repositories {
  mavenCentral()
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "${JavaVersion.VERSION_11}"
    }
  }
}

kotlin {
  jvm()
  sourceSets {
    named("jvmMain") {
      dependencies {
        implementation(project(":common"))
        implementation("io.ktor:ktor-client-cio:_")
        implementation("io.ktor:ktor-client-auth:_")
        implementation("org.kodein.di:kodein-di:_")
        implementation("org.jsoup:jsoup:_")
        implementation("ch.qos.logback:logback-classic:_")
        implementation(kotlin("reflect"))
        implementation("org.jetbrains.kotlinx:kotlinx-cli:_")
      }
    }
    named("jvmTest") {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:_")
      }
    }
    all {
      languageSettings.apply {
        useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
        useExperimentalAnnotation("io.ktor.util.KtorExperimentalAPI")
        useExperimentalAnnotation("kotlin.time.ExperimentalTime")
        useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
        useExperimentalAnnotation("kotlinx.cli.ExperimentalCli")
      }
    }
  }
}

val mainClassName = "scanner.IndexKt"

tasks {
  val compileKotlinJvm by getting
  val jvmProcessResources by getting
  register<JavaExec>("run") {
    group = "run"
    mainClass.set(mainClassName)
    dependsOn(compileKotlinJvm, jvmProcessResources)
    classpath = files(
      configurations["jvmRuntimeClasspath"],
      compileKotlinJvm.outputs,
      jvmProcessResources.outputs
    )
  }
  named("jvmJar", Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.WARN
    val classpath = configurations["jvmRuntimeClasspath"].files.map { if (it.isDirectory) it else zipTree(it) }
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
        "Created-By" to "${GradleVersion.current()}",
        "Created-From" to Git.headCommitHash
      )
    }

    inputs.property("mainClassName", mainClassName)
    inputs.files(classpath)
    outputs.file(archiveFile)
  }
}
