package kamp.domain

import kotlinx.serialization.*

public interface MavenArtifact {
  public val group: String
  public val name: String
  public val latestVersion: String
  public val releaseVersion: String?
  public val versions: List<String>?
  public val lastUpdated: Long?

  public val path: String get() = "$group:$name:$releaseVersion"
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
