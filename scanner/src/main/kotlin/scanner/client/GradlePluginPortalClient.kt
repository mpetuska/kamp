package scanner.client

import io.ktor.client.*
import kamp.domain.*
import kotlinx.serialization.json.*
import org.jsoup.nodes.*
import scanner.domain.*

class GradlePluginPortalClient(
  override val client: HttpClient,
  override val json: Json,
) : MavenRepositoryClient<MavenArtifactImpl>(Repository.GRADLE_PLUGIN_PORTAL.url) {
  override fun parsePage(page: Document): List<String>? = page.getElementsByTag("a")
    ?.mapNotNull { elm ->
      elm.takeIf { it.hasAttr("href") }?.attr("href")
    }
}
