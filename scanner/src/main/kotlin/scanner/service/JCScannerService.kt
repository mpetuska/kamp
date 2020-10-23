package scanner.service

import kamp.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.domain.jc.*
import scanner.util.*

object JCScannerService : ScannerService<JCArtifact>() {
  override val client = JCenterClient
  
  private val seed = ('a'..'z') + ('0'..'9') + ('A'..'Z')
  private val extendedSeed = listOf('.', '-') + seed
  
  private suspend fun SendChannel<JCPage>.scanGroup(groupPrefix: String, artifactPrefix: String? = null): Int =
    client.getPageCount(groupPrefix, artifactPrefix)?.let { count ->
      logger.info { "Scanned JCenter API with g=$groupPrefix* ${if (artifactPrefix != null) "a=$artifactPrefix*" else ""} -> $count pages" }
      if (count >= client.maxPageCount) {
        artifactPrefix?.let {
          extendedSeed.fold(0) { acc, char ->
            acc + scanGroup(groupPrefix, "$artifactPrefix$char")
          }
        } ?: run {
          var pagesFound = 0
          extendedSeed.forEach {
            pagesFound += scanGroup("$groupPrefix$it")
          }
          if (pagesFound > 0) {
            pagesFound
          } else {
            seed.fold(0) { acc, char ->
              acc + scanGroup(groupPrefix, "$char")
            }
          }
        }
      } else {
        for (page in 0 until count) {
          send(JCPage(page, groupPrefix, artifactPrefix))
        }
        count
      }
    } ?: 0
  
  override fun CoroutineScope.produceArtifactChannel(): ReceiveChannel<JCArtifact> = produce(capacity = 16) {
    val pages = produce(Dispatchers.Default, capacity = 100) {
      seed.forEach {
        scanGroup("$it")
      }
    }
    parallel {
      pages.consumeSafe { page ->
        logger.debug { "Fetching JC artifacts for $page" }
        client.getArtifacts(page.page, page.groupPrefix, page.artifactPrefix)?.forEach {
          send(it)
        }
      }
    }
  }
  
  data class JCPage(val page: Int, val groupPrefix: String, val artifactPrefix: String? = null)
  
  override suspend fun buildMppLibrary(
    pomDetails: PomDetails,
    targets: Set<KotlinTarget>,
    artifact: JCArtifact,
  ): KotlinMPPLibrary? {
    var pkgFetched = false
    var pkg: JCenterClient.JCPackageResponse? = null
    suspend fun pkg() = if (pkgFetched) {
      pkg
    } else {
      pkg = client.getPackage(artifact)
      pkgFetched = true
      pkg
    }
    val (description, website, scm) = PomDetails(
      description = pomDetails.description ?: pkg()?.desc,
      website = pomDetails.website ?: pkg()?.websiteUrl ?: pkg()?.issueTrackerUrl,
      scm = pomDetails.scm ?: pkg()?.vcsUrl
    )
    
    KotlinMPPLibrary(
      group = artifact.group,
      name = artifact.name,
      latestVersion = artifact.latestVersion,
      targets = targets,
      description = description,
      website = website,
      scm = scm
    )
    return super.buildMppLibrary(pomDetails, targets, artifact)
  }
}
