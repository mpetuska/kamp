package scanner.domain

import org.kodein.di.DirectDIAware
import org.kodein.di.instance
import scanner.client.ArtifactoryClient
import scanner.client.JBossClient
import scanner.client.MavenRepositoryClient
import shared.domain.MavenArtifactImpl

enum class Repository(
  val alias: String,
  val url: String,
  val client: DirectDIAware.(url: String) -> MavenRepositoryClient<MavenArtifactImpl>,
) {
  MAVEN_CENTRAL(
    "mavenCentral",
    "https://repo1.maven.org/maven2",
    { ArtifactoryClient(it, instance(), instance()) }
  ),
  GRADLE_PLUGIN_PORTAL(
    "gradlePluginPortal",
    "https://plugins.gradle.org/m2",
    { ArtifactoryClient(it, instance(), instance()) }
  ),
  SPRING(
    "spring",
    "https://repo.spring.io/release",
    { ArtifactoryClient(it, instance(), instance()) }
  ),
  ATLASSIAN(
    "atlassian",
    "https://packages.atlassian.com/content/repositories/atlassian-public",
    { ArtifactoryClient(it, instance(), instance()) }
  ),
  J_BOSS(
    "jBoss",
    "https://repository.jboss.org/nexus/content/repositories/releases",
    { JBossClient(it, instance(), instance()) }
  ),
  HORTON_WORKS(
    "hortonWorks",
    "https://repo.hortonworks.com/content/repositories/releases",
    { JBossClient(it, instance(), instance()) }
  ),
}
