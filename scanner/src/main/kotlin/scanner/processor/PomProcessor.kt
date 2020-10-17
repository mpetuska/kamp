package scanner.processor

import org.jsoup.nodes.*

object PomProcessor {
  fun getDescription(pom: Document) = pom.selectFirst("project>description")?.text()
  
  fun getUrl(pom: Document) = pom.selectFirst("project>url")?.text()
  
  fun getScmUrl(pom: Document) = pom.selectFirst("project>scm")?.let {
    it.selectFirst("connection")?.text()
      ?: it.selectFirst("url")?.text()
  }
}
