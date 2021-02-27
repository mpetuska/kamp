package scanner.client

import io.ktor.client.*
import org.jsoup.nodes.*
import scanner.domain.mc.*

class MavenCentralClient(override val client: HttpClient) : MavenRepositoryClient<MCArtifact>("https://repo1.maven.org/maven2") {
  override fun parsePage(page: Document): List<String>? = page.getElementById("contents")
    ?.getElementsByTag("a")
    ?.mapNotNull { elm ->
      elm.takeIf { it.hasAttr("title") }?.attr("title")
    }
}
