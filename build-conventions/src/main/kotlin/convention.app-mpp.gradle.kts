import ext.MppAppExtension
import util.Git

plugins {
  id("convention.mpp")
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
  val compileKotlinJvm by getting
  val jvmProcessResources by getting
  named<Jar>("jvmJar") {
    duplicatesStrategy = DuplicatesStrategy.WARN
    val jvmClasspath = jvmRuntimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    from(jvmClasspath) { exclude("META-INF/**") }
    manifest {
      attributes(
        "Main-Class" to mppApp.jvmMainClass.get(),
        "Built-By" to System.getProperty("user.name"),
        "Build-Jdk" to System.getProperty("java.version"),
        "Implementation-Version" to project.version,
        "Created-By" to "${GradleVersion.current()}",
        "Created-From" to Git.headCommitHash
      )
    }
    inputs.property("mainClassName", mppApp.jvmMainClass)
    inputs.files(jvmRuntimeClasspath)
  }
  register<JavaExec>("jvmRun") {
    dependsOn(compileKotlinJvm, jvmProcessResources, jvmRuntimeClasspath)
    classpath(compileKotlinJvm, jvmProcessResources, jvmRuntimeClasspath)
    mainClass.set(mppApp.jvmMainClass)
    systemProperty("io.ktor.development", "true")

    inputs.property("mainClassName", mppApp.jvmMainClass)
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
