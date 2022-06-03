import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import ext.MppAppExtension
import util.Git

plugins {
  id("convention.mpp")
  id("com.github.johnrengelman.shadow")
}

val mppApp = extensions.create("mppApp", MppAppExtension::class, project)

kotlin {
  jvm()
  js(IR) {
    binaries.executable()
  }
}

tasks {
  val jvmRuntimeClasspath = configurations.named("jvmRuntimeClasspath")
  val compileKotlinJvm = named("compileKotlinJvm")
  val jvmProcessResources = named("jvmProcessResources")
  val fatJar = register<ShadowJar>("fatJar") {
    onlyIf { mppApp.fatJar.get() }
    group = "build"
    manifest {
      attributes(
        mapOf(
          "Built-By" to System.getProperty("user.name"),
          "Build-Jdk" to System.getProperty("java.version"),
          "Implementation-Version" to project.version,
          "Created-By" to "${GradleVersion.current()}",
          "Created-From" to Git.headCommitHash
        ) + (mppApp.jvmMainClass.orNull?.let { mapOf("Main-Class" to it) } ?: mapOf())
      )
    }
    archiveAppendix.set("jvm")
    archiveClassifier.set("fat")
    from(compileKotlinJvm)
    println(project.configurations.map { it.name })
    configurations.add(jvmRuntimeClasspath.get())
    inputs.property("mainClassName", mppApp.jvmMainClass)
  }
  build {
    dependsOn(fatJar)
  }
  register<JavaExec>("jvmRun") {
    onlyIf { mppApp.jvmMainClass.isPresent }
    dependsOn(compileKotlinJvm, jvmProcessResources, jvmRuntimeClasspath)
    classpath(compileKotlinJvm, jvmProcessResources, jvmRuntimeClasspath)
    mppApp.jvmMainClass.orNull?.let {
      mainClass.set(it)
      inputs.property("mainClassName", it)
    }
    systemProperty("io.ktor.development", "true")

    inputs.files(jvmRuntimeClasspath, compileKotlinJvm, jvmProcessResources)
  }
}

afterEvaluate {
  tasks {
    withType<JavaExec> {
      group = "run"
    }
    names.filter { it.startsWith("js") && it.endsWith("Run") }.forEach {
      named(it) {
        group = "run"
      }
    }
  }
}
