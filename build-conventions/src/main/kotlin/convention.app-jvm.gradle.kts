import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import ext.AppExtension
import ext.JvmAppExtension
import util.Git

plugins {
  id("convention.app-common")
  id("convention.library-jvm")
  id("com.github.johnrengelman.shadow")
}

val jvmApp = the<AppExtension>().extensions.create<JvmAppExtension>("jvm").apply {
  fatJar.convention(true)
}

tasks {
  val jvmRuntimeClasspath = configurations.named("jvmRuntimeClasspath")
  val compileKotlinJvm = named("compileKotlinJvm")
  val jvmProcessResources = named("jvmProcessResources")
  val fatJar = register<ShadowJar>("jvmFatJar") {
    onlyIf { jvmApp.fatJar.get() }
    group = "build"
    manifest {
      attributes(
        mapOf(
          "Built-By" to System.getProperty("user.name"),
          "Build-Jdk" to System.getProperty("java.version"),
          "Implementation-Version" to project.version,
          "Created-By" to "${GradleVersion.current()}",
          "Created-From" to Git.headCommitHash
        ) + (jvmApp.mainClass.orNull?.let { mapOf("Main-Class" to it) } ?: mapOf())
      )
    }
    mergeServiceFiles()
    archiveAppendix.set("jvm")
    archiveClassifier.set("fat")
    from(compileKotlinJvm, jvmProcessResources)
    configurations.add(jvmRuntimeClasspath.get())
    inputs.property("mainClassName", jvmApp.mainClass)
  }
  assemble {
    dependsOn(fatJar)
  }
  register<JavaExec>("jvmRun") {
    onlyIf { jvmApp.mainClass.isPresent }
    classpath(compileKotlinJvm, jvmProcessResources, jvmRuntimeClasspath)
    mainClass.set(jvmApp.mainClass)
    inputs.property("mainClass", jvmApp.mainClass)
  }
  withType<JavaExec> {
    group = "run"
  }
}
