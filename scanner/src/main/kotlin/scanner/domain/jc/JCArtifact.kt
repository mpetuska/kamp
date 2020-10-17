package scanner.domain.jc

import kotlinx.serialization.Serializable
import scanner.domain.*

@Serializable
data class JCArtifact(
  val pkg: JCPackage,
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  val lastUpdated: String? = null,
) : MavenArtifact()
