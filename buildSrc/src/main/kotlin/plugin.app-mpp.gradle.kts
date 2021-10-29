plugins {
  id("plugin.common")
  kotlin("multiplatform")
}

kotlin {
  sourceSets {
    findByName("commonTest")?.apply {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    findByName("jvmTest")?.apply {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
    findByName("jsTest")?.apply {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}
