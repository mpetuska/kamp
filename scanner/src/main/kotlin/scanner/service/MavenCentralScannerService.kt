package scanner.service

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import scanner.client.*
import scanner.domain.mc.*
import scanner.processor.*

class MavenCentralScannerService(
  override val client: MavenRepositoryClient<MCArtifact>,
  pomProcessor: PomProcessor,
  gradleModuleProcessor: GradleModuleProcessor,
) : MavenScannerService<MCArtifact>(pomProcessor, gradleModuleProcessor) {
  
  private suspend fun ProducerScope<MCArtifact>.scanRepoPage(page: List<MavenRepositoryClient.RepoItem>) {
    val mavenMetadata = page.find { it.value == "maven-metadata.xml" }
    if (mavenMetadata != null) {
      client.getArtifactDetails(mavenMetadata.path)?.let {
        send(MCArtifact(it.group, it.name, it.latestVersion))
      }
    } else {
      channelFlow {
        page
          .filter(MavenRepositoryClient.RepoItem::isDirectory)
          .forEach {
            logger.info("Scanning MC page ${it.path}")
            client.listRepositoryPath(it.path)?.let { item -> send(item) }
          }
      }.collect { scanRepoPage(it) }
    }
  }
  
  override val artifacts: Flow<MCArtifact> = channelFlow {
    channelFlow {
      client.listRepositoryPath("")
        ?.filter(MavenRepositoryClient.RepoItem::isDirectory)
        ?.forEach {
          client.listRepositoryPath(it.path)?.let { item -> send(item) }
        }
    }.collect { scanRepoPage(it) }
  }
}
