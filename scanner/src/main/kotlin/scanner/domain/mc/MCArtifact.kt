package scanner.domain.mc

import kamp.domain.*
import kotlinx.serialization.*

@Serializable
data class MCArtifact(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
) : MavenArtifact()
