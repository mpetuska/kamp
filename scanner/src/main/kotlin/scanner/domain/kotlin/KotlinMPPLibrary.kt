package scanner.domain.kotlin

import kotlinx.serialization.*
import scanner.domain.*

@Serializable
data class KotlinMPPLibrary(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  val targets: Set<KotlinTarget>,
  val description: String?,
  val website: String?,
  val scm: String?,
) : MavenArtifact()
