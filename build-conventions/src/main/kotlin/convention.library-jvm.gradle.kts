import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("convention.library-common")
}

kotlin {
  jvm {
    compilations {
      val main by getting
      val instrumentedTest by creating {
        defaultSourceSet {
          dependencies {
            implementation(main.compileDependencyFiles + main.output.classesDirs)
          }
        }
      }
      val testTask = tasks.register<Test>("jvmInstrumentedTest") {
        group = "verification"
        classpath = instrumentedTest.compileDependencyFiles +
          instrumentedTest.runtimeDependencyFiles +
          instrumentedTest.output.allOutputs
        testClassesDirs = instrumentedTest.output.classesDirs
        shouldRunAfter("jvmTest")
        testLogging { events("passed") }
      }
      tasks.named("allTests") { dependsOn(testTask) }
    }
  }
  sourceSets {
    jvmTest {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
    jvmInstrumentedTest {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation(project(":lib:test"))
      }
    }
  }
}

tasks {
  withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
  }
}
