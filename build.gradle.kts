import org.jetbrains.kotlin.gradle.tasks.*

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("org.jlleitschuh.gradle.ktlint")
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
    mavenCentral()
    maven("https://dl.bintray.com/kodein-framework/kodein-dev")
    maven("https://maven.pkg.github.com/mpetuska/khakra") {
      credentials {
        username = project.properties["gpr.username"]?.toString() ?: System.getenv("GH_PKG_USER")
        password = project.properties["gpr.password"]?.toString() ?: System.getenv("GH_PKG_PASSWORD")
      }
    }
    jcenter()
    mavenLocal()
  }
  tasks {
    withType<Test> {
      useJUnitPlatform()
    }
    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = "15"
      kotlinOptions {
        useIR = true
        jvmTarget = "${JavaVersion.VERSION_15}"
      }
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
