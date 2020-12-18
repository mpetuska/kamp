plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("com.palantir.graal")
  application
}

val jsModuleName = "kamp-$version"
kotlin {
  sourceSets {
    main {
      dependencies {
        implementation(project(rootProject.path))
        implementation("io.ktor:ktor-server-cio:1.4.1")
        implementation("org.slf4j:slf4j-simple:1.7.30")
//        implementation("ch.qos.logback:logback-classic:1.2.3")
//        implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.2.3")
      }
    }
    test {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:4.3.0")
      }
    }
  }
}

application {
  mainClass.set("app.IndexKt")
}

graal {
  graalVersion("20.3.0")
  javaVersion("11")
  mainClass(application.mainClass.get())
  outputName("${project.name}-native")
  option("--no-fallback")
  option("--report-unsupported-elements-at-runtime")
  option("--verbose")
  if (project.hasProperty("alpine")) {
    option("--static")
    option("--libc=musl")
  } else {
    option("-H:+StaticExecutableWithDynamicLibC")
  }
  option("-H:Log=registerResource")
  option("-H:-UseServiceLoaderFeature")
}

tasks {
  val browserDistribution = getByPath("client:browserDistribution")
  jar {
    group = "build"
    dependsOn(browserDistribution)
    into("WEB-INF") {
      from(browserDistribution)
    }
    doLast {
      copy {
        from(browserDistribution)
        into(File(archiveFile.get().asFile.parentFile, "WEB-INF"))
      }
    }
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
    inputs.files(browserDistribution.outputs)
    outputs.file(archiveFile)
  }
  
  val buildNativeImage by creating(Exec::class) {
    group = "build"
    dependsOn(jar)
    val fatJarfile = jar.get().archiveFile.get().asFile
    val outputFile = buildDir.resolve("bin/${fatJarfile.nameWithoutExtension}")
    val cmd = mutableListOf(
      "native-image",
      "--no-fallback",
      "--report-unsupported-elements-at-runtime",
      "-H:Log=registerResource",
      "-H:-UseServiceLoaderFeature",
      "-H:+PrintAnalysisCallTree",
      "--verbose",
      "-jar",
      fatJarfile.absolutePath,
      "$outputFile"
    )
    val alpine = project.hasProperty("alpine")
    if (alpine) {
      cmd.add("--static")
      cmd.add("--libc=musl")
    } else {
      cmd.add("-H:+StaticExecutableWithDynamicLibC")
    }
    workingDir(buildDir)
    commandLine(cmd)
    inputs.file(fatJarfile)
    inputs.property("alpine", alpine)
    outputs.file(outputFile)
  }
  
  create("diagnoseNativeConfig", JavaExec::class) {
    dependsOn(jar)
    jvmArgs(
      "-agentlib:native-image-agent=config-merge-dir=$buildDir/graalvm"
    )
    classpath(jar.get().archiveFile)
  }
  
  val runNativeImage by creating(Exec::class) {
    group = "run"
    dependsOn(buildNativeImage)
    workingDir(buildDir)
    executable(buildNativeImage.outputs.files.first())
    args(
      "-Dorg.slf4j.simpleLogger.defaultLogLevel=trace"
    )
  }
}
