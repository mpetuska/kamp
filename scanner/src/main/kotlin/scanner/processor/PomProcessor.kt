package scanner.processor

import org.jsoup.nodes.*


class PomProcessor {
  val Document.description get() = selectFirst("project>description")?.text()
  
  val Document.url get() = selectFirst("project>url")?.text()
  
  val Document.scmUrl
    get() = selectFirst("project>scm")?.let {
      val url = it.selectFirst("url")?.text()
        ?: it.selectFirst("connection")?.text()
      val main = url?.trim()?.split("://")?.getOrNull(1)?.removeSuffix(".git")?.removeSuffix("/")
      "https://$main.git"
    }
}
