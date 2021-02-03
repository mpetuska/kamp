package kamp.domain

public interface MavenArtifact {
  public val group: String
  public val name: String
  public val latestVersion: String
  
  public val path: String get() = "$group:$name:$latestVersion"
}
