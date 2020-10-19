package kamp.domain

import kotlinx.serialization.*

@Serializable
public abstract class MavenArtifact {
  public abstract val group: String
  public abstract val name: String
  public abstract val latestVersion: String
  
  public val path: String by lazy { "$group:$name:$latestVersion" }
}
