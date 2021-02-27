package scanner.processor

import org.jsoup.nodes.*


class PomProcessor {
  val Document.description: String?
    get() = selectFirst("project>description")?.text()
  
  val Document.url: String?
    get() = selectFirst("project>url")?.text()
  
  val Document.scmUrl: String?
    get() = selectFirst("project>scm")?.let {
      val url = it.selectFirst("url")?.text()
        ?: it.selectFirst("connection")?.text()
        ?: it.selectFirst("developerConnection")?.text()
      url?.trim()
        ?.split("://")
        ?.let { chunks ->
          chunks.getOrNull(1)
            ?.removeSuffix(".git")
            ?.removeSuffix("/")
            ?.let { u -> "${chunks[0]}://$u.git" }
        }
        ?: url
    }
}
