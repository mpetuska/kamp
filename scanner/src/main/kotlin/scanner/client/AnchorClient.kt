package scanner.client

import domain.MavenArtifact
import org.jsoup.nodes.Document

abstract class AnchorClient<T : MavenArtifact>(
  val url: String,
) : MavenRepositoryClient<T>(url) {
  abstract fun String.isBackLink(): Boolean

  override fun parsePage(page: Document): List<String>? =
    page.getElementsByTag("a").mapNotNull { elm ->
      val text = elm.text()
      text.takeIf { it.isNotBlank() && !it.isBackLink() }
    }
}
