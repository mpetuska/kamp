package scanner.client

import io.ktor.client.*
import kamp.domain.*
import kotlinx.serialization.json.*
import org.jsoup.nodes.*
import scanner.domain.*

class MavenCentralClient(
  override val client: HttpClient,
  override val json: Json,
) : MavenRepositoryClient<MavenArtifactImpl>(Repository.MAVEN_CENTRAL.url) {
  override fun parsePage(page: Document): List<String>? = page.getElementById("contents")
    ?.getElementsByTag("a")
    ?.mapNotNull { elm ->
      elm.takeIf { it.hasAttr("title") }?.attr("title")?.takeUnless { it.startsWith("..") }
    }
}
