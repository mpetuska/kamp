package dev.petuska.kodex.cli.cmd.scan.domain

import dev.petuska.kodex.cli.cmd.scan.client.ArtifactoryClient
import dev.petuska.kodex.cli.cmd.scan.client.JBossClient
import dev.petuska.kodex.cli.cmd.scan.client.MavenRepositoryClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

enum class Repository(
  val alias: String,
  val url: String,
  val client: (
    url: String,
    client: HttpClient,
    json: Json,
  ) -> MavenRepositoryClient<SimpleMavenArtefact>,
) {
  MAVEN_CENTRAL(
    "mavenCentral",
    "https://repo1.maven.org/maven2",
    ::ArtifactoryClient
  ),
  GRADLE_PLUGIN_PORTAL(
    "gradlePluginPortal",
    "https://plugins.gradle.org/m2",
    ::ArtifactoryClient
  ),
  SPRING(
    "spring",
    "https://repo.spring.io/release",
    ::ArtifactoryClient
  ),
  ATLASSIAN(
    "atlassian",
    "https://packages.atlassian.com/content/repositories/atlassian-public",
    ::ArtifactoryClient
  ),
  J_BOSS(
    "jBoss",
    "https://repository.jboss.org/nexus/content/repositories/releases",
    ::JBossClient
  ),
  HORTON_WORKS(
    "hortonWorks",
    "https://repo.hortonworks.com/content/repositories/releases",
    ::JBossClient
  ),
}
