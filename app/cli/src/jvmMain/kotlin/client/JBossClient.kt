package dev.petuska.kamp.cli.client

import dev.petuska.kamp.core.domain.SimpleMavenArtefact
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

class JBossClient(
  url: String,
  override val client: HttpClient,
  override val json: Json,
) : AnchorClient<SimpleMavenArtefact>(url) {
  override fun String.isBackLink(): Boolean = equals("Parent Directory", true)
}
