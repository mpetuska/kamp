package kamp.domain

import kotlinx.serialization.*

@Serializable
public data class KotlinMPPLibrary(
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  val targets: Set<KotlinTarget>,
  val description: String?,
  val website: String?,
  val scm: String?,
) : MavenArtifact {
  @Transient
  val isMultiplatform: Boolean = targets.size > 1
}
