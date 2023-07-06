package dev.petuska.kodex.cli.cmd.scan.client

import dev.petuska.kodex.core.domain.MavenArtefact
import org.jsoup.nodes.Document

abstract class AnchorClient<T : MavenArtefact>(
  val url: String,
) : MavenRepositoryClient<T>(url) {
  abstract fun String.isBackLink(): Boolean

  override fun parsePage(page: Document): List<String>? =
    page.getElementsByTag("a").mapNotNull { elm ->
      val text = elm.attr("href")
      text.takeIf { it.isNotBlank() && !elm.text().isBackLink() }
    }
}
