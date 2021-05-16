package scanner.client

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import shared.domain.MavenArtifactImpl

class JBossClient(
  url: String,
  override val client: HttpClient,
  override val json: Json,
) : AnchorClient<MavenArtifactImpl>(url) {
  override fun String.isBackLink(): Boolean = equals("Parent Directory", true)
}
