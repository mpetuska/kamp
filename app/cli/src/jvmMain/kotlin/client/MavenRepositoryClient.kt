package dev.petuska.kamp.cli.client

import dev.petuska.kamp.cli.domain.GradleModule
import dev.petuska.kamp.cli.util.LoggerDelegate
import dev.petuska.kamp.cli.util.asDocument
import dev.petuska.kamp.cli.util.supervisedAsync
import dev.petuska.kamp.core.domain.MavenArtefact
import dev.petuska.kamp.core.domain.SimpleMavenArtefact
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.nodes.Document

abstract class MavenRepositoryClient<A : MavenArtefact>(
  private val defaultRepositoryRootUrl: String,
) : Closeable {
  protected abstract fun parsePage(page: Document): List<String>?
  protected abstract val client: HttpClient
  protected abstract val json: Json
  private val logger by LoggerDelegate()

  private val A.mavenModuleRootUrl: String
    get() = "$defaultRepositoryRootUrl/${group.replace(".", "/")}/$name"
  private val A.mavenModuleVersionUrl: String
    get() = "$mavenModuleRootUrl/$releaseVersion"

  suspend fun getArtifactDetails(pathToMavenMetadata: String): SimpleMavenArtefact? =
    coroutineScope {
      val artifact = supervisedAsync {
        val url = "$defaultRepositoryRootUrl$pathToMavenMetadata"
        val pom = client.get(url).bodyAsText().asDocument()
        val doc = pom.getElementsByTag("metadata").first()!!
        runCatching {
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
          SimpleMavenArtefact(
            group = doc.selectFirst("groupId")!!.text(),
            name = doc.selectFirst("artifactId")!!.text(),
            latestVersion = doc.selectFirst("versioning>latest")?.text()
              ?: doc.selectFirst("version")!!.text(),
            releaseVersion = doc.selectFirst("versioning>release")?.text(),
            versions = doc.selectFirst("versioning>versions")?.children()?.map { v -> v.text() },
            lastUpdated = lastUpdated,
          )
        }.onFailure {
          if (doc.selectFirst("plugins") == null) {
            logger.warn("Unable to parse maven-metadata.xml from $url")
          }
        }.getOrNull()
      }

      artifact.await()
    }

  suspend fun getGradleModule(artifact: A): GradleModule? = coroutineScope {
    supervisedAsync {
      val module =
        client.get("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.releaseVersion}.module")
          .bodyAsText()
      json.decodeFromString<GradleModule>(module)
    }
      .await()
  }

  suspend fun getMavenPom(artifact: A): Document? = coroutineScope {
    supervisedAsync {
      client
        .get("${artifact.mavenModuleVersionUrl}/${artifact.name}-${artifact.releaseVersion}.pom")
        .bodyAsText()
        .asDocument()
    }
      .await()
  }

  suspend fun listRepositoryPath(path: String): List<RepoItem>? = coroutineScope {
    supervisedAsync {
      client.get("$defaultRepositoryRootUrl${path.removeSuffix("/")}/").bodyAsText().let { str ->
        parsePage(str.asDocument())?.map { RepoItem(it, path) }
      }
    }
      .await()
  }

  override fun close() = client.close()

  class RepoItem(val value: String, path: String) {
    val parentPath = "/${path.removeSuffix("/").removePrefix("/")}"
    val path = "${if (parentPath == "/") "" else parentPath}/$value".removeSuffix("/")
    val isDirectory = value.endsWith("/")
    val isFile = !isDirectory

    override fun toString() = value
  }
}
