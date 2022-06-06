package dev.petuska.kamp.cli.cmd.scan.util

import dev.petuska.kamp.cli.cmd.scan.domain.RepoDirectory
import dev.petuska.kamp.cli.cmd.scan.domain.RepoItem.Companion.SEP
import dev.petuska.kamp.core.domain.MavenArtefact

fun MavenArtefact.gradleMetadataFileName(version: String = this.version): String = "$name-$version.module"
fun MavenArtefact.mavenPomFileName(version: String = this.version): String = "$name-$version.pom"

fun MavenArtefact.moduleRootDirectory(repositoryRootUrl: String) =
  RepoDirectory.fromPath(
    repositoryRootUrl,
    "${group.replace(".", SEP)}$SEP$name",
  )

fun MavenArtefact.moduleVersionDirectory(repositoryRootUrl: String, version: String = this.version) =
  moduleRootDirectory(repositoryRootUrl).dir(version)

fun MavenArtefact.gradleMetadataFile(repositoryRootUrl: String, version: String = this.version) =
  moduleVersionDirectory(repositoryRootUrl, version).file(gradleMetadataFileName(version))

fun MavenArtefact.mavenPomFile(repositoryRootUrl: String, version: String = latestVersion) =
  moduleVersionDirectory(repositoryRootUrl, version).file(mavenPomFileName(version))
