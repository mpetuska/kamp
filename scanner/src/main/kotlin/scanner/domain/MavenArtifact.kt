package scanner.domain

import kotlinx.serialization.Serializable

@Serializable
abstract class MavenArtifact {
  abstract val group: String
  abstract val name: String
  abstract val latestVersion: String
  
  val path by lazy { "$group:$name:$latestVersion" }
}
