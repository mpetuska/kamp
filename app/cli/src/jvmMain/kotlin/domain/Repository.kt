package dev.petuska.kamp.cli.domain

import dev.petuska.kamp.cli.client.ArtifactoryClient
import dev.petuska.kamp.cli.client.JBossClient
import dev.petuska.kamp.cli.client.MavenRepositoryClient
import dev.petuska.kamp.core.domain.SimpleMavenArtefact
import org.kodein.di.DirectDIAware
import org.kodein.di.instance

enum class Repository(
  val alias: String,
  val url: String,
  val client: DirectDIAware.(url: String) -> MavenRepositoryClient<SimpleMavenArtefact>,
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
