plugins {
  id("convention.mpp")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api("org.kodein.di:kodein-di:_")
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
        api("org.jetbrains.kotlinx:kotlinx-serialization-cbor:_")
      }
    }
    jvmMain {
      dependencies {
        implementation("ch.qos.logback:logback-classic:_")
        api("org.slf4j:slf4j-api:_")
      }
    }
  }
}
