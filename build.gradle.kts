plugins {
  kotlin("multiplatform") version "1.4.10"
  kotlin("plugin.serialization") version "1.4.0"
  id("com.github.johnrengelman.shadow") version "6.1.0"
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
    mavenLocal()
  }
  tasks {
    withType<Test> {
      useJUnitPlatform()
    }
  }
}

kotlin {
  explicitApi()
  jvm()
  js(BOTH) { browser() }
  
  sourceSets {
    named("commonMain") {
      dependencies {
        api("io.ktor:ktor-client-serialization:1.4.1")
      }
    }
  }
}
