package dev.petuska.kamp.cli.processor

import org.jsoup.nodes.Document

class PomProcessor {
  val Document.description: String?
    get() = selectFirst("project>description")?.text()

  val Document.url: String?
    get() = selectFirst("project>url")?.text()

  val Document.scmUrl: String?
    get() =
        selectFirst("project>scm")?.let {
          val url =
              run {
                    it.selectFirst("url")?.text()
                        ?: it.selectFirst("connection")?.text()
                            ?: it.selectFirst("developerConnection")?.text()
                  }
                  ?.trim()

          val path =
              url?.trim()?.split("://")?.getOrNull(1)
                  ?: url?.split("@")?.getOrNull(1)?.replaceFirst(":", "/")

          path?.removeSuffix(".git")?.removeSuffix("/")?.let { u -> "https://$u.git" } ?: url
        }
}
