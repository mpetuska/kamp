package dev.petuska.kodex.cli.cmd.scan.client

import dev.petuska.kodex.cli.cmd.scan.domain.SimpleMavenArtefact
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

class JBossClient(
  url: String,
  override val client: HttpClient,
  override val json: Json,
) : AnchorClient<SimpleMavenArtefact>(url) {
  override fun String.isBackLink(): Boolean = equals("Parent Directory", true)
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
