package scanner.client

import org.jsoup.nodes.*
import scanner.domain.mc.*

object MavenCentralClient : MavenRepositoryClient<MCArtifact>("https://repo1.maven.org/maven2") {
  override fun parsePage(page: Document): List<String>? = page.getElementById("contents")
    ?.getElementsByTag("a")
    ?.mapNotNull { elm ->
      elm.takeIf { it.hasAttr("title") }?.attr("title")
    }
}
