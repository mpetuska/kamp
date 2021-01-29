plugins {
  kotlin("multiplatform") version Version.kotlin
  kotlin("plugin.serialization") version Version.kotlin
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
  js {
    useCommonJs()
    browser()
  }
  
  sourceSets {
    named("commonMain") {
      dependencies {
        api("io.ktor:ktor-client-serialization:${Version.ktor}")
      }
    }
  }
}
