package scanner.client

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.jsoup.nodes.*
import scanner.domain.jc.*
import scanner.util.*


interface JCApi<P> {
  suspend fun getPageCount(): Int?
  suspend fun getPackages(page: Int = 0): List<P>?
  suspend fun getPackageArtifacts(pkg: P): List<JCArtifact>?
}

object JCenterClient : MavenRepositoryClient<JCArtifact>(), JCApi<JCPackage> by WebAPI {
  object OfficialAPI : JCApi<String> {
    private const val basicAuth =
      "Basic bXAtY2k6MDcyOWM2MTZmZjcwOWI4MzgwMWZhN2UxZjQ3M2NiMTRmZTA2NTlmNQ=="
    
    private const val pageSize = 50
    override suspend fun getPageCount(): Int? = httpClientWithNotFound {
      val res = get<HttpResponse>("https://api.bintray.com/repos/bintray/jcenter/packages?start_pos=0")
      res.headers["X-RangeLimit-Total"]?.toIntOrNull()?.let { it / pageSize }
    }
    
    override suspend fun getPackages(page: Int): List<String>? {
      return httpClientWithNotFound {
        val res =
          get<JsonArray>("https://api.bintray.com/repos/bintray/jcenter/packages?start_pos=${pageSize * page}") {
            header(HttpHeaders.Authorization, basicAuth)
          }
        res.mapNotNull { it.jsonObject["name"]?.jsonPrimitive?.content }
      }
    }
    
    override suspend fun getPackageArtifacts(pkg: String): List<JCArtifact>? =
      JCenterClient.getPackageArtifacts(pkg)
  }
  
  object WebAPI : JCApi<JCPackage> {
    private const val pageSize = 250
    private const val baseUrl =
      "https://bintray.com/api/ui/repo/bintray/jcenter/packages?\$ignore_error=true&order=asc&sort_by=lowerCaseName"
    
    override suspend fun getPageCount(): Int? = httpClientWithNotFound {
      val res = get<JCResponse>("$baseUrl&limit=1&offset=0")
      res.recordsTotal?.let { it / pageSize }
    }
    
    override suspend fun getPackages(page: Int): List<JCPackage>? = httpClientWithNotFound {
      val res = get<JCResponse>("$baseUrl&limit=$pageSize&offset=${page * pageSize}")
      res.items?.mapNotNull {
        it.path
          ?.removePrefix("/")
          ?.split("/")
          ?.takeIf { d -> d.size == 3 }
          ?.let { (subject, repo, name) ->
            JCPackage(
              subject = subject,
              repo = repo,
              name = name
            )
          }
      }
    }
    
    override suspend fun getPackageArtifacts(pkg: JCPackage): List<JCArtifact>? =
      JCenterClient.getPackageArtifacts(pkg)
    
    @Serializable
    data class JCResponse(
      @SerialName("hasWritePermission")
      val hasWritePermission: Boolean? = null,
      @SerialName("items")
      val items: List<Item>? = null,
      @SerialName("recordsFiltered")
      val recordsFiltered: Int? = null,
      @SerialName("recordsTotal")
      val recordsTotal: Int? = null,
    ) {
      @Serializable
      data class Item(
        @SerialName("avatar")
        val avatar: Avatar? = null,
        @SerialName("canCreateVersion")
        val canCreateVersion: Boolean? = null,
        @SerialName("canEditPkg")
        val canEditPkg: Boolean? = null,
        @SerialName("description")
        val description: String? = null,
        @SerialName("downloadListContainsArtifacts")
        val downloadListContainsArtifacts: Boolean? = null,
        @SerialName("hasWritePermission")
        val hasWritePermission: Boolean? = null,
        @SerialName("isLink")
        val isLink: Boolean? = null,
        @SerialName("lastUpdated")
        val lastUpdated: String? = null,
        @SerialName("latestVersionName")
        val latestVersionName: String? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("path")
        val path: String? = null,
        @SerialName("rank")
        val rank: Int? = null,
        @SerialName("score")
        val score: Score? = null,
        @SerialName("totalDownloads")
        val totalDownloads: Int? = null,
        @SerialName("totalDownloadsForLastMonth")
        val totalDownloadsForLastMonth: Int? = null,
      ) {
        @Serializable
        data class Avatar(
          @SerialName("editUrl")
          val editUrl: String? = null,
          @SerialName("hasWritePermission")
          val hasWritePermission: Boolean? = null,
          @SerialName("url")
          val url: String? = null,
        )
        
        @Serializable
        data class Score(
          @SerialName("avg")
          val avg: Double? = null,
          @SerialName("ratingCount")
          val ratingCount: Int? = null,
          @SerialName("total")
          val total: Int? = null,
        )
      }
    }
  }
  
  override val JCArtifact.repositoryRootUrl: String
    get() = "https://dl.bintray.com/${pkg.subject}/${pkg.repo}"
  override val defaultRepositoryRootUrl: String
    get() = "https://dl.bintray.com/bintray/jcenter"
  
  override fun parsePage(page: Document): List<String> = page.getElementsByTag("a").map { it.text() }
  
  suspend fun getPackageArtifacts(pkgName: String): List<JCArtifact>? =
    getPackageArtifacts(JCPackage("bintray", "jcenter", pkgName))
  
  override suspend fun getPackageArtifacts(pkg: JCPackage): List<JCArtifact>? = httpClientWithNotFound {
    get<JCPackageResponse>("https://api.bintray.com/packages/${pkg.subject}/${pkg.repo}/${pkg.name}").run {
      latestVersion?.takeIf { "null" != it }?.let { latestVersion ->
        val updated = updated
        systemIds
          ?.mapNotNull { it.split(":").takeIf { c -> c.size >= 2 } }
          ?.mapNotNull { (group, name) ->
            val details = listOfNotNull(
              owner,
              repo,
              name,
            ).takeIf { it.size == 3 }
            
            details?.let {
              val (subject, repo, pName) = it
              JCArtifact(
                pkg = JCPackage(
                  subject = subject,
                  repo = repo,
                  name = pName,
                ),
                group = group,
                name = name,
                latestVersion = latestVersion,
                lastUpdated = updated
              )
            }
          }
      }
    }
    
  }
  
  @Serializable
  data class JCPackageResponse(
    @SerialName("created")
    val created: String? = null,
    @SerialName("desc")
    val desc: String? = null,
    @SerialName("followers_count")
    val followersCount: Int? = null,
    @SerialName("issue_tracker_url")
    val issueTrackerUrl: String? = null,
    @SerialName("latest_version")
    val latestVersion: String? = null,
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
}
