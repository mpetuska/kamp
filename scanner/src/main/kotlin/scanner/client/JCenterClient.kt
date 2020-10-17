package scanner.client

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import org.jsoup.*
import org.jsoup.nodes.*
import scanner.domain.jc.*
import scanner.util.*


object JCenterClient : MavenRepositoryClient<JCArtifact>() {
  suspend fun getPackages(page: Int = 0): List<JCPackage>? = httpClientWithNotFound {
    submitForm<String>("https://bintray.com/bintray/jcenter", Parameters.build {
      this["offset"] = "$page"
      this["max"] = "8"
      this["repoPath"] = "/bintray/jcenter"
      this["sortBy"] = "lowerCaseName"
      this["filterByPkgName"] = ""
    })
  }?.let { Jsoup.parse(it) }?.let { html ->
    html.getElementsByClass("package-name").map { p ->
      p.child(0).let {
        val (subject, repo, name) = it.attr("href").removePrefix("/").split("/")
        JCPackage(subject, repo, name)
      }
    }
  }
  
  suspend fun getPackageArtifacts(pkg: JCPackage): List<JCArtifact>? = httpClientWithNotFound {
    val res = get<JsonObject>("https://api.bintray.com/packages/${pkg.subject}/${pkg.repo}/${pkg.name}")
    
    res["latest_version"]?.jsonPrimitive?.content?.takeIf { "null" != it }?.let { latestVersion ->
      val updated = res["updated"]?.jsonPrimitive?.content
      res["system_ids"]?.jsonArray
        ?.mapNotNull { it.jsonPrimitive.content.split(":").takeIf { c -> c.size >= 2 } }
        ?.map { (group, name) ->
          JCArtifact(
            pkg = JCPackage(
              subject = res["owner"]?.jsonPrimitive?.content ?: pkg.subject,
              repo = res["repo"]?.jsonPrimitive?.content ?: pkg.repo,
              name = res["name"]?.jsonPrimitive?.content ?: pkg.name
            ),
            group = group,
            name = name,
            latestVersion = latestVersion,
            lastUpdated = updated
          )
        }
    }
  }
  
  override val JCArtifact.repositoryRootUrl: String
    get() = "https://dl.bintray.com/${pkg.subject}/${pkg.repo}"
  override val defaultRepositoryRootUrl: String
    get() = "https://dl.bintray.com/bintray/jcenter"
  
  override fun parsePage(page: Document): List<String> = page.getElementsByTag("a").map { it.text() }
}
