plugins {
  id("dev.petuska.klip")
  kotlin("multiplatform")
  id("plugin.common")
  id("io.kotest.multiplatform")
}

afterEvaluate {
  kotlin {
    targets.all {
      compilations.all {
        kotlinOptions {
          verbose = true
        }
      }
    }
    sourceSets {
      findByName("commonTest")?.apply {
        dependencies {
          dependencies {
            implementation("io.kotest:kotest-framework-engine:_")
            implementation("io.kotest:kotest-assertions-core:_")
            implementation("io.kotest:kotest-property:_")
            implementation("dev.petuska:klip:_")
          }
        }
        findByName("jvmTest")?.apply {
          dependencies {
            implementation("io.kotest:kotest-runner-junit5:_")
          }
        }
        findByName("jsTest")?.apply {
          dependencies {
          }
        }
      }
    }
  }
}
