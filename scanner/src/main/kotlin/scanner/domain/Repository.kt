package scanner.domain

enum class Repository(
  val alias: String,
  val url: String,
) {
  MAVEN_CENTRAL("mavenCentral", "https://repo1.maven.org/maven2"),
  GRADLE_PLUGIN_PORTAL("gradlePluginPortal", "https://plugins.gradle.org/m2"),
  J_BOSS("jBoss", "https://repository.jboss.org/nexus/content/repositories/releases"),
  SPRING("spring", "https://repo.spring.io/release"),
  HORTON_WORKS("hortonWorks", "https://repo.hortonworks.com/content/repositories/releases"),
}
