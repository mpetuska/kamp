package domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

interface MavenArtifact {
  val group: String
  val name: String
  val latestVersion: String
  val releaseVersion: String?
  val versions: List<String>?
  val lastUpdated: Long?

  @Transient
  val version: String
    get() = releaseVersion ?: latestVersion

  @Transient
  val path: String
    get() = "$group:$name:$version"
}

@Serializable
data class MavenArtifactImpl(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  override val releaseVersion: String?,
  override val versions: List<String>?,
  override val lastUpdated: Long?,
) : MavenArtifact
