import org.jetbrains.kotlin.gradle.tasks.*

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("com.bnorm.react.kotlin-react-function") apply false
  idea
}

allprojects {
  group = "lt.petuska"
  version = "0.0.1"
  apply(plugin = "idea")
  idea {
    module {
      isDownloadSources = true
      isDownloadJavadoc = true
    }
  }
  repositories {
    jcenter()
    mavenCentral()
    maven("https://dl.bintray.com/patternfly-kotlin/patternfly-fritz2")
    maven("https://dl.bintray.com/kodein-framework/kodein-dev")
    maven("https://maven.pkg.github.com/mpetuska/khakra") {
      credentials {
        username = project.properties["gpr.username"]?.toString() ?: System.getenv("GH_PKG_USER")
        password = project.properties["gpr.password"]?.toString() ?: System.getenv("GH_PKG_PASSWORD")
      }
    }
    mavenLocal()
  }
  tasks {
    withType<Test> {
      useJUnitPlatform()
    }
    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = "15"
    }
  }
}

kotlin {
  explicitApi()
  jvm()
  js {
    browser()
  }
  
  sourceSets {
    named("commonMain") {
      dependencies {
        api("io.ktor:ktor-client-serialization:_")
      }
    }
  }
}
