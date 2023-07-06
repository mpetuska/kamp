import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import ext.AppExtension
import ext.JvmAppExtension
import util.Git

plugins {
  id("convention.app-common")
  id("com.github.johnrengelman.shadow")
}

val app = the<AppExtension>().extensions.create<JvmAppExtension>("jvm").apply {
  fatJar.convention(true)
}

kotlin {
  jvm()
}

tasks {
  val runtimeClasspath = configurations.named("jvmRuntimeClasspath")
  val compileKotlin = named("compileKotlinJvm")
  val processResources = named("jvmProcessResources")
  val fatJar = register<ShadowJar>("jvmFatJar") {
    onlyIf { app.fatJar.get() }
    group = "build"
    manifest {
      attributes(
        mapOf(
          "Built-By" to System.getProperty("user.name"),
          "Build-Jdk" to System.getProperty("java.version"),
          "Implementation-Version" to project.version,
          "Created-By" to "${GradleVersion.current()}",
          "Created-From" to Git.headCommitHash
        ) + (app.mainClass.orNull?.let { mapOf("Main-Class" to it) } ?: mapOf())
      )
    }
    mergeServiceFiles()
    archiveAppendix.set("jvm")
    archiveClassifier.set("fat")
    from(compileKotlin, processResources)
    configurations.add(runtimeClasspath.get())
    inputs.property("mainClassName", app.mainClass)
  }
  assemble {
    dependsOn(fatJar)
  }
  register<JavaExec>("jvmRun") {
    onlyIf { app.mainClass.isPresent }
    classpath(compileKotlin, processResources, runtimeClasspath)
    mainClass.set(app.mainClass)
    inputs.property("mainClass", app.mainClass)
  }
  withType<JavaExec> {
    group = "run"
  }
}
