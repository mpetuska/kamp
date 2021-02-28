package scanner.client

import io.ktor.client.*
import kamp.domain.*
import kotlinx.serialization.json.*
import org.jsoup.nodes.*

class MavenCentralClient(
  override val client: HttpClient,
  override val json: Json,
) : MavenRepositoryClient<MavenArtifactImpl>("https://repo1.maven.org/maven2") {
  override fun parsePage(page: Document): List<String>? = page.getElementById("contents")
    ?.getElementsByTag("a")
    ?.mapNotNull { elm ->
      elm.takeIf { it.hasAttr("title") }?.attr("title")
    }
}
