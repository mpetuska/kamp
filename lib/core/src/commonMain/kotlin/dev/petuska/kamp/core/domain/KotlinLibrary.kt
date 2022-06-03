package dev.petuska.kamp.core.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class KotlinLibrary(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  override val releaseVersion: String?,
  override val versions: List<String>?,
  override val lastUpdated: Long?,
  val targets: Set<KotlinTarget>,
  val description: String?,
  val website: String?,
  val scm: String?,
) : MavenArtefact {
  constructor(
    artefact: MavenArtefact,
    targets: Set<KotlinTarget>,
    description: String?,
    website: String?,
    scm: String?,
  ) : this(
    group = artefact.group,
    name = artefact.name,
    latestVersion = artefact.latestVersion,
    releaseVersion = artefact.releaseVersion,
    versions = artefact.versions,
    lastUpdated = artefact.lastUpdated,
    targets = targets,
    description = description,
    website = website,
    scm = scm
  )

  @Suppress("PropertyName", "unused", "VariableNaming")
  val _id: String = "$group:$name"

  @Transient
  val isMultiplatform: Boolean = targets.size > 1
}
