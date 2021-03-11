package scanner.domain

enum class Repository(
  val alias: String,
  val url: String,
) {
  MAVEN_CENTRAL("mavenCentral", "https://repo1.maven.org/maven2"),
  GRADLE_PLUGIN_PORTAL("gradlePluginPortal", "https://plugins.gradle.org/m2")
}
