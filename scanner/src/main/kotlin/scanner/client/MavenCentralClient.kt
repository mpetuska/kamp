package scanner.client

import org.jsoup.nodes.*
import org.kodein.di.*
import scanner.domain.mc.*

class MavenCentralClient(override val di: DI) : MavenRepositoryClient<MCArtifact>("https://repo1.maven.org/maven2") {
  override fun parsePage(page: Document): List<String>? = page.getElementById("contents")
    ?.getElementsByTag("a")
    ?.mapNotNull { elm ->
      elm.takeIf { it.hasAttr("title") }?.attr("title")
    }
}
