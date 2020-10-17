package scanner.domain.mc

import kotlinx.serialization.Serializable
import scanner.domain.*

@Serializable
data class MCArtifact(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
) : MavenArtifact()
