plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  application
}

kotlin {
  dependencies {
    implementation(project(rootProject.path))
    implementation("io.ktor:ktor-client-cio:${Version.ktor}")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    
    testImplementation("io.kotest:kotest-runner-junit5:4.3.0")
  }
  sourceSets.all {
    languageSettings.apply {
      useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
      useExperimentalAnnotation("io.ktor.util.KtorExperimentalAPI")
    }
  }
}

application {
  mainClassName = "scanner.IndexKt"
}

tasks {
  jar {
    val classpath = configurations.compileClasspath.get().files.map { if (it.isDirectory) it else zipTree(it) }
    from(classpath) {
      exclude("META-INF/*.SF")
      exclude("META-INF/*.DSA")
      exclude("META-INF/*.RSA")
    }
    
    manifest {
      attributes(
        "Main-Class" to application.mainClassName,
        "Implementation-Version" to project.version
      )
    }
    
    inputs.files(classpath)
    outputs.file(archiveFile)
  }
}
