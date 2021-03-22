package scanner.client

import io.ktor.client.*
import kamp.domain.*
import kotlinx.serialization.json.*

class JBossClient(
    url: String,
    override val client: HttpClient,
    override val json: Json,
) : AnchorClient<MavenArtifactImpl>(url) {
  override fun String.isBackLink(): Boolean = equals("Parent Directory", true)
}
