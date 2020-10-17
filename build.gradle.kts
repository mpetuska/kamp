plugins {
  kotlin("multiplatform") version "1.4.10"
  kotlin("plugin.serialization") version "1.4.0"
  id("com.github.johnrengelman.shadow") version "6.1.0"
  application
}

allprojects {
  repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
  }
}

kotlin {
  jvm {
    withJava()
  }
  js {
    binaries.executable()
    browser()
  }
  
  sourceSets {
    val jvmMain by getting {
      dependencies {
      }
    }
    val jvmTest by getting {
      dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test")
        implementation("org.jetbrains.kotlin:kotlin-test-junit")
      }
    }
  }
}

application {
  mainClassName = "kamp.AppKt"
}
