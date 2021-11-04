package dev.petuska.kamp.core.domain

data class SimpleMavenArtefact(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  override val releaseVersion: String?,
  override val versions: List<String>?,
  override val lastUpdated: Long?,
) : MavenArtefact
