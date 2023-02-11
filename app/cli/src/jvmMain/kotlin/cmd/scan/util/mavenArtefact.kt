package dev.petuska.kodex.cli.cmd.scan.util

import dev.petuska.kodex.cli.cmd.scan.domain.RepoDirectory
import dev.petuska.kodex.cli.cmd.scan.domain.RepoItem.Companion.SEP
import dev.petuska.kodex.core.domain.MavenArtefact

fun MavenArtefact.gradleMetadataFileName(version: String = this.version): String =
  "$name-$version.module"

fun MavenArtefact.mavenPomFileName(version: String = this.version): String = "$name-$version.pom"

fun MavenArtefact.moduleRootDirectory() = RepoDirectory.fromPath(
  "${group.replace(".", SEP)}$SEP$name",
)

fun MavenArtefact.moduleVersionDirectory(version: String = this.version) =
  moduleRootDirectory().dir(version)

fun MavenArtefact.gradleMetadataFile(version: String = this.version) =
  moduleVersionDirectory(version).file(gradleMetadataFileName(version))

fun MavenArtefact.mavenPomFile(version: String = latestVersion) =
  moduleVersionDirectory(version).file(mavenPomFileName(version))
