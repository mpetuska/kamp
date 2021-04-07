package kamp.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

public interface MavenArtifact {
  public val group: String
  public val name: String
  public val latestVersion: String
  public val releaseVersion: String?
  public val versions: List<String>?
  public val lastUpdated: Long?

  @Transient
  public val version: String
    get() = releaseVersion ?: latestVersion

  @Transient
  public val path: String
    get() = "$group:$name:$version"
}

@Serializable
public data class MavenArtifactImpl(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  override val releaseVersion: String?,
  override val versions: List<String>?,
  override val lastUpdated: Long?,
) : MavenArtifact
