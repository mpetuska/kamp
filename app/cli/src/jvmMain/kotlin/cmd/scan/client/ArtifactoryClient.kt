package dev.petuska.kamp.cli.cmd.scan.client

import dev.petuska.kamp.cli.cmd.scan.domain.SimpleMavenArtefact
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

class ArtifactoryClient(
  url: String,
  override val client: HttpClient,
  override val json: Json,
) : AnchorClient<SimpleMavenArtefact>(url) {
  override fun String.isBackLink(): Boolean = startsWith("..")
  override fun buildArtefact(
    group: String,
    name: String,
    latestVersion: String,
    releaseVersion: String?,
    versions: List<String>?,
    lastUpdated: Long?
  ) = SimpleMavenArtefact(
    group = group,
    name = name,
    latestVersion = latestVersion,
    releaseVersion = releaseVersion,
    versions = versions,
    lastUpdated = lastUpdated,
  )
}
