package scanner.client

import kotlinx.serialization.*
import org.jsoup.nodes.*
import scanner.domain.jc.*
import scanner.util.*


object JCenterClient : MavenRepositoryClient<JCArtifact>() {
  private val logger by Logger()
  private const val pageSize = 50
  const val maxPageCount = 10000 / pageSize
  private const val apiUrl = "https://api.bintray.com"
  private val JCPackage.apiPackageDetailsUrl get() = "$apiUrl/packages/${subject}/${repo}/${name}"
  private const val apiMavenSearchUrl = "$apiUrl/search/packages/maven"
  
  suspend fun getPageCount(groupPrefix: String, artifactPrefix: Char? = null): Int? =
    request("$apiMavenSearchUrl?start_pos=0&g=$groupPrefix*${artifactPrefix?.let { "&a=$it*" } ?: ""}")?.let { res ->
      res.headers["X-RangeLimit-Total"]?.toIntOrNull()?.let { it / pageSize }
    }?.let {
      if (it > maxPageCount) {
        logger.warn { "Exceeded max page count [$maxPageCount] for package query with group prefix [$groupPrefix] and artifact prefix[$artifactPrefix]" }
        maxPageCount
      } else it
    }
  
  suspend fun getArtifacts(page: Int, groupPrefix: String, artifactPrefix: Char? = null): List<JCArtifact>? =
    fetch<List<JCResponse>>(
      "$apiMavenSearchUrl?start_pos=${pageSize * page}&g=$groupPrefix*${artifactPrefix?.let { "&a=$it*" } ?: ""}"
    )?.let { res ->
      res.mapNotNull { jcr ->
        listOfNotNull(jcr.owner, jcr.repo, jcr.name).takeIf { it.size == 3 }?.let { (subject, repo, name) ->
          JCPackage(subject, repo, name)
        }?.let { jcPackage ->
          jcr.latestVersion?.let { version ->
            jcr.systemIds?.mapNotNull {
              it.split(":").takeIf { c -> c.size >= 2 }?.let { (group, artifact) ->
                JCArtifact(jcPackage, group, artifact, version)
              }
            }
          }
        }
      }.flatten()
    }
  
  suspend fun getPackage(artifact: JCArtifact) = fetch<JCPackageResponse>(artifact.pkg.apiPackageDetailsUrl)
  
  override fun parsePage(page: Document): List<String> = page.getElementsByTag("a").map { it.text() }
  
  override val JCArtifact.repositoryRootUrl: String
    get() = "https://dl.bintray.com/${pkg.subject}/${pkg.repo}"
  override val defaultRepositoryRootUrl: String
    get() = "https://dl.bintray.com/bintray/jcenter"
  
  @Serializable
  data class JCPackageResponse(
    @SerialName("attribute_names")
    val attributeNames: List<String>? = null,
    @SerialName("created")
    val created: String? = null,
    @SerialName("desc")
    val desc: String? = null,
    @SerialName("followers_count")
    val followersCount: Int? = null,
    @SerialName("issue_tracker_url")
    val issueTrackerUrl: String? = null,
    @SerialName("labels")
    val labels: List<String>? = null,
    @SerialName("latest_version")
    val latestVersion: String? = null,
    @SerialName("licenses")
    val licenses: List<String?>? = null,
    @SerialName("linked_to_repos")
    val linkedToRepos: List<String>? = null,
    @SerialName("maturity")
    val maturity: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("owner")
    val owner: String? = null,
    @SerialName("rating_count")
    val ratingCount: Int? = null,
    @SerialName("repo")
    val repo: String? = null,
    @SerialName("system_ids")
    val systemIds: List<String>? = null,
    @SerialName("updated")
    val updated: String? = null,
    @SerialName("vcs_url")
    val vcsUrl: String? = null,
    @SerialName("versions")
    val versions: List<String>? = null,
    @SerialName("website_url")
    val websiteUrl: String? = null,
  )
  
  @Serializable
  data class JCResponse(
    @SerialName("desc")
    val desc: String? = null,
    @SerialName("latest_version")
    val latestVersion: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("owner")
    val owner: String? = null,
    @SerialName("repo")
    val repo: String? = null,
    @SerialName("system_ids")
    val systemIds: List<String>? = null,
    @SerialName("versions")
    val versions: List<String>? = null,
  )
}
