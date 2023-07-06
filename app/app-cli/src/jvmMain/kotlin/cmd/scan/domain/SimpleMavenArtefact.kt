package dev.petuska.kodex.cli.cmd.scan.domain

import dev.petuska.kodex.core.domain.MavenArtefact

data class SimpleMavenArtefact(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  override val releaseVersion: String?,
  override val versions: List<String>?,
  override val lastUpdated: Long?,
) : MavenArtefact {
  override fun toString(): String = "$group:$name"
}
