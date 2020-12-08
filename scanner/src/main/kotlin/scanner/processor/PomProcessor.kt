package scanner.processor

import org.jsoup.nodes.*

object PomProcessor {
  fun getDescription(pom: Document) = pom.selectFirst("project>description")?.text()
  
  fun getUrl(pom: Document) = pom.selectFirst("project>url")?.text()
  
  fun getScmUrl(pom: Document) = pom.selectFirst("project>scm")?.let {
    val url = it.selectFirst("url")?.text()
      ?: it.selectFirst("connection")?.text()
    val main = url?.trim()?.split("://")?.getOrNull(1)?.removeSuffix(".git")?.removeSuffix("/")
    "https://$main.git"
  }
}
