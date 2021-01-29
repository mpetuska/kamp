plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  application
}

application {
  mainClassName = "app.IndexKt"
}

kotlin {
  sourceSets {
    main {
      dependencies {
        implementation(project(rootProject.path))
        implementation("io.ktor:ktor-server-cio:${Version.ktor}")
        implementation("org.slf4j:slf4j-simple:1.7.30")
        implementation("com.microsoft.azure:applicationinsights-web-auto:${Version.applicationInsights}")
//        implementation("ch.qos.logback:logback-classic:1.2.3")
//        implementation("org.litote.kmongo:kmongo-coroutine-serialization:${Version.kmongo}")
      }
    }
    test {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:4.3.0")
      }
    }
  }
}

tasks {
  val browserDistribution = getByPath("client:browserDistribution")
  jar {
    dependsOn(browserDistribution)
    into("WEB-INF") {
      from(browserDistribution)
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
    
    inputs.property("mainClassName", application.mainClassName)
    inputs.files(classpath)
    inputs.files(browserDistribution.outputs)
  }
}
