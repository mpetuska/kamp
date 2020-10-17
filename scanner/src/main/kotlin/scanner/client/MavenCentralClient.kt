package scanner.client

import org.jsoup.nodes.*
import scanner.domain.mc.*

object MavenCentralClient : MavenRepositoryClient<MCArtifact>() {
  override val defaultRepositoryRootUrl: String = "https://repo1.maven.org/maven2"
  override val MCArtifact.repositoryRootUrl: String
    get() = this@MavenCentralClient.defaultRepositoryRootUrl
  
  override fun parsePage(page: Document): List<String> = page.getElementById("contents")
    ?.getElementsByTag("a")
    ?.mapNotNull { elm ->
      elm.takeIf { it.hasAttr("title") }?.let {
        it.attr("title")
      }
    } ?: listOf()
}
