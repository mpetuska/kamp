package dev.petuska.kodex.cli.cmd.scan.client

import dev.petuska.kodex.cli.cmd.scan.domain.*
import dev.petuska.kodex.cli.cmd.scan.domain.RepoItem.Companion.SEP
import dev.petuska.kodex.cli.cmd.scan.util.gradleMetadataFile
import dev.petuska.kodex.cli.cmd.scan.util.mavenPomFile
import dev.petuska.kodex.cli.util.asDocument
import dev.petuska.kodex.core.domain.MavenArtefact
import dev.petuska.kodex.core.util.logger
import dev.petuska.kodex.repository.util.runCatchingIO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.nodes.Document

abstract class MavenRepositoryClient<A : MavenArtefact>(
  private val repositoryRootUrl: String,
) : Closeable {
  protected abstract fun parsePage(page: Document): List<String>?

  @Suppress("LongParameterList")
  protected abstract fun buildArtefact(
    group: String,
    name: String,
    latestVersion: String,
    releaseVersion: String?,
    versions: List<String>?,
    lastUpdated: Long?
  ): A

  protected abstract val client: HttpClient
  protected abstract val json: Json
  private val logger = logger()

  suspend fun getMavenArtefact(mavenMetadata: RepoFile): FileData<A>? = supervisorScope {
    val pom = getMavenPom(mavenMetadata)
    val doc = pom?.data?.getElementsByTag("metadata")?.first()
    doc?.let {
      runCatching {
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
          val latestVersion = doc.selectFirst("versioning>latest")?.text()
            ?: doc.selectXpath("//version").first()?.text()
            ?: versions.last()
          buildArtefact(
            // https://repo1.maven.org/maven2/com/inmobi/monetization/inmobi-mediation/maven-metadata.xml
            group = doc.selectFirst("groupId")?.text() ?: doc.selectFirst("groupdId")!!.text(),
            name = doc.selectFirst("artifactId")!!.text(),
            latestVersion = latestVersion,
            releaseVersion = doc.selectFirst("versioning>release")?.text(),
            versions = versions,
            lastUpdated = lastUpdated,
          ).let(pom.file::data)
        }
      }.onFailure {
        if (doc.selectFirst("plugins") == null) {
          logger.error(
            "Unable to parse maven-metadata.xml from ${
            mavenMetadata.url(
              repositoryRootUrl
            )
            }",
            it
          )
        }
      }.getOrNull()
    }
  }

  suspend fun getGradleModule(artifact: A): FileData<GradleModule>? =
    getGradleModule(artifact.gradleMetadataFile())

  suspend fun getGradleModule(file: RepoFile): FileData<GradleModule>? = supervisorScope {
    val url = file.url(repositoryRootUrl)
    runCatchingIO {
      logger.debug("Looking for gradle module in $url")
      client.get(url).takeIf { it.status != HttpStatusCode.NotFound }?.bodyAsText()?.let { module ->
        json.decodeFromString<GradleModule>(module)
      }?.let(file::data)
    }.onFailure {
      logger.error("Unable to extract Gradle metadata file from $url", it)
    }.getOrNull()
  }

  suspend fun getMavenPom(artifact: A): FileData<Document>? = getMavenPom(artifact.mavenPomFile())

  suspend fun getMavenPom(file: RepoFile): FileData<Document>? = supervisorScope {
    val url = file.url(repositoryRootUrl)
    runCatchingIO {
      client.get(url).takeIf { it.status != HttpStatusCode.NotFound }?.bodyAsText()?.asDocument()
        ?.let(file::data)
    }.onFailure {
      logger.error("Unable to extract Maven pom file from $url", it)
    }.getOrNull()
  }

  suspend fun listRepositoryPath(dir: RepoDirectory): List<RepoItem>? = supervisorScope {
    val urls = dir.url(repositoryRootUrl).let { listOf(it + SEP, it) }
    var result: List<RepoItem>? = null
    for (url in urls) {
      result = runCatchingIO {
        client.get(url)
          .takeIf { it.status != HttpStatusCode.NotFound }
          ?.bodyAsText()
          ?.asDocument()
          ?.let(::parsePage)
          ?.map(dir::item)
      }.onFailure {
        logger.error("Failed to list repository path at $url", it)
      }.getOrNull()
      if (result != null) break
    }
    result
  }

  override fun close() {
    client.close()
  }
}
