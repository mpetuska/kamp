package dev.petuska.kamp.cli.cmd.scan.service

import dev.petuska.kamp.cli.cmd.scan.client.MavenRepositoryClient
import dev.petuska.kamp.cli.cmd.scan.domain.FileData
import dev.petuska.kamp.cli.cmd.scan.domain.RepoDirectory
import dev.petuska.kamp.cli.cmd.scan.domain.RepoFile
import dev.petuska.kamp.cli.cmd.scan.domain.SimpleMavenArtefact
import dev.petuska.kamp.cli.cmd.scan.processor.GradleModuleProcessor
import dev.petuska.kamp.cli.cmd.scan.processor.PomProcessor
import dev.petuska.kamp.core.domain.KotlinLibrary
import dev.petuska.kamp.core.util.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class SimpleMavenArtefactService(
  private val client: MavenRepositoryClient<SimpleMavenArtefact>,
) {
  private val logger = logger()

  fun findMavenArtefacts(pages: Flow<RepoDirectory.Listed>): Flow<FileData<SimpleMavenArtefact>> =
    pages.mapNotNull { page ->
      logger.debug("Looking for maven-metadata.xml in $page")
      val files = page.items.filterIsInstance<RepoFile>()
      val artefact = files.find { it.name == "maven-metadata.xml" }?.let {
        client.getMavenArtefact(it)
      }
      artefact?.also {
        logger.debug("Found maven artefact [$it] in $page")
      }
    }

  fun findKotlinLibraries(artefacts: Flow<FileData<SimpleMavenArtefact>>): Flow<FileData<KotlinLibrary>> =
    artefacts.mapNotNull { artefact ->
      with(GradleModuleProcessor()) {
        val module = client.getGradleModule(artefact.file)?.takeIf { it.data.isRootModule }
        val supportedTargets = module?.data?.supportedTargets?.takeIf { it.isNotEmpty() }
        supportedTargets?.let {
          client.getMavenPom(artefact.file)?.data?.let { pom ->
            with(PomProcessor()) {
              KotlinLibrary(
                targets = it,
                artefact = artefact.data,
                description = pom.description,
                website = pom.url,
                scm = pom.scmUrl,
              ).let(module.file::data)
            }
          }
        }
      }
    }
}
