package dev.petuska.kamp.cli.cmd.scan.client

import dev.petuska.kamp.cli.cmd.scan.domain.GradleModule
import dev.petuska.kamp.cli.cmd.scan.domain.RepositoryItem
import dev.petuska.kamp.cli.cmd.scan.domain.SimpleMavenArtefact
import dev.petuska.kamp.cli.util.asDocument
import dev.petuska.kamp.core.domain.MavenArtefact
import dev.petuska.kamp.core.util.logger
import dev.petuska.kamp.repository.util.runCatchingIO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.nodes.Document

abstract class MavenRepositoryClient<A : MavenArtefact>(
  private val defaultRepositoryRootUrl: String,
) : Closeable {
  protected abstract fun parsePage(page: Document): List<String>?
  protected abstract val client: HttpClient
  protected abstract val json: Json
  private val logger = logger()

  private val A.mavenModuleRootUrl: String
    get() = "$defaultRepositoryRootUrl/${group.replace(".", "/")}/$name"
  private val A.mavenModuleVersionUrl: String
    get() = "$mavenModuleRootUrl/$version"

  suspend fun getMavenArtefact(pathToMavenMetadata: String): SimpleMavenArtefact? = supervisorScope {
    val url = "$defaultRepositoryRootUrl$pathToMavenMetadata"
    val pom = getMavenPom(url)
    val doc = pom?.getElementsByTag("metadata")?.first()
    doc?.let {
      runCatchingIO {
        val versions = doc.selectFirst("versioning>versions")?.children()?.map { v -> v.text() }
        versions?.let {
          val lastUpdated = doc.selectFirst("versioning>lastUpdated")?.text()?.let {
            GMTDate(
              year = it.substring(0 until 4).toInt(),
              month = Month.from(it.substring(4 until 6).toInt() - 1),
              dayOfMonth = it.substring(6 until 8).toInt(),
              hours = it.substring(8 until 10).toInt(),
              minutes = it.substring(10 until 12).toInt(),
              seconds = it.substring(12 until 14).toInt(),
            ).timestamp
          }
          val latestVersion =
            doc.selectFirst("versioning>latest")?.text()
              ?: doc.selectXpath("//version").first()?.text() ?: versions.last()
          SimpleMavenArtefact(
            // https://repo1.maven.org/maven2/com/inmobi/monetization/inmobi-mediation/maven-metadata.xml
            group = doc.selectFirst("groupId")?.text() ?: doc.selectFirst("groupdId")!!.text(),
            name = doc.selectFirst("artifactId")!!.text(),
            latestVersion = latestVersion,
            releaseVersion = doc.selectFirst("versioning>release")?.text(),
            versions = versions,
            lastUpdated = lastUpdated,
          )
        }
      }.onFailure {
        if (doc.selectFirst("plugins") == null) {
          logger.error("Unable to parse maven-metadata.xml from $url", it)
        }
      }.getOrNull()
    }
  }

  suspend fun getGradleModule(artifact: A): GradleModule? = supervisorScope {
    val url = "${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.version}.module"
    runCatchingIO {
      logger.debug("Looking for gradle module in $url")
      client.get(url).takeIf { it.status.isSuccess() }?.bodyAsText()?.let { module ->
        json.decodeFromString<GradleModule>(module)
      }
    }.onFailure {
      logger.error("Unable to extract Gradle metadata file from $url", it)
    }.getOrNull()
  }

  suspend fun getMavenPom(artifact: A): Document? =
    getMavenPom("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.releaseVersion}.pom")

  suspend fun getMavenPom(url: String): Document? = supervisorScope {
    runCatchingIO {
      client.get(url).bodyAsText().asDocument()
    }.onFailure {
      logger.error("Unable to extract Maven pom file from $url", it)
    }.getOrNull()
  }

  suspend fun listRepositoryPath(path: String): List<RepositoryItem>? = supervisorScope {
    runCatchingIO {
      client.get("$defaultRepositoryRootUrl${path.removeSuffix("/")}/").bodyAsText().let { str ->
        parsePage(str.asDocument())?.map {
          RepositoryItem(it, path)
        }
      }
    }.getOrNull()
  }

  override fun close() = client.close()
}
