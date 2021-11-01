plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("gradle-plugin", "_"))
  implementation(kotlin("serialization", "_"))
  implementation("com.diffplug.spotless:spotless-plugin-gradle:_")
  implementation("com.github.jakemarsden:git-hooks-gradle-plugin:_")
  implementation("dev.petuska:klip-gradle-plugin:_")
  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle:5.0.0.5")
}
