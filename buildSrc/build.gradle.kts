plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.diffplug.spotless:spotless-plugin-gradle:_")
  implementation(kotlin("gradle-plugin", "_"))
  implementation(kotlin("serialization", "_"))
  implementation("com.github.jakemarsden:git-hooks-gradle-plugin:_")
}
