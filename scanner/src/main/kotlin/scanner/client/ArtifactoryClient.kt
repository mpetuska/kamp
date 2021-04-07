package scanner.client

import io.ktor.client.HttpClient
import kamp.domain.MavenArtifactImpl
import kotlinx.serialization.json.Json

class ArtifactoryClient(
  url: String,
  override val client: HttpClient,
  override val json: Json,
) : AnchorClient<MavenArtifactImpl>(url) {
  override fun String.isBackLink(): Boolean = startsWith("..")
}
