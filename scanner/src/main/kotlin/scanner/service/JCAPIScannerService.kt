package scanner.service

import kamp.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import scanner.client.*
import scanner.domain.jc.*
import scanner.util.*
import java.io.*

object JCAPIScannerService : ScannerService<JCArtifact>() {
  override val client = JCenterMavenClient
  
  private val seed = ('a'..'z') + ('0'..'9') + ('A'..'Z')
  private val extendedSeed = seed + '.' + '-'
  
  private suspend fun SendChannel<String>.scanGroup(groupPrefix: String, output: SendChannel<JCPage>) {
    client.getPageCount(groupPrefix)?.let { count ->
      logger.debug { "Scanned JCenter API with g=$groupPrefix* -> $count pages" }
      if (count >= client.maxPageCount) {
        extendedSeed.forEach {
          send("$groupPrefix$it")
        }
      } else {
        for (page in 0 until count) {
          output.send(JCPage(page, groupPrefix))
        }
      }
    }
  }
  
  override suspend fun buildMppLibrary(
    pomDetails: PomDetails,
    targets: Set<KotlinTarget>,
    artifact: JCArtifact,
  ): KotlinMPPLibrary? {
    var pkgFetched = false
    var pkg: JCenterMavenClient.JCPackageResponse? = null
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
  
  override fun CoroutineScope.produceArtifactChannel(): ReceiveChannel<JCArtifact> = produce(capacity = 16) {
    val pages = produce<JCPage>(capacity = 8) {
      val pagesChannel = this
      val groupPrefixes = Channel<String>(16)
      launch {
        seed.forEach {
          groupPrefixes.send("$it")
        }
      }
      val cancelJob = launch {
        var emptyCount = 0
        do {
          delay(30 * 1000)
          val prefix = groupPrefixes.poll()
          if (prefix != null) {
            emptyCount = 0
            groupPrefixes.send(prefix)
          } else {
            emptyCount++
          }
        } while (emptyCount < 2)
        groupPrefixes.close()
      }
      parallel {
        groupPrefixes.consumeSafe {
          groupPrefixes.scanGroup(it, pagesChannel)
        }
      }
      cancelJob.join()
    }
    parallel {
      pages.consumeSafe { page ->
        logger.debug { "Fetching JC artifacts for $page" }
        client.getArtifacts(page.page, page.groupPrefix, page.artifactPrefix)?.forEach {
          Logger.appendToFile(File("out/jcAPI.log")) {
            "${it.fullPath}\n"
          }
          send(it)
        }
      }
    }
  }
  
  data class JCPage(val page: Int, val groupPrefix: String, val artifactPrefix: Char? = null)
}
