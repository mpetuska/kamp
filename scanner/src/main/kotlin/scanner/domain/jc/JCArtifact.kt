package scanner.domain.jc

import kamp.domain.*
import kotlinx.serialization.*

@Serializable
data class JCArtifact(
  val pkg: JCPackage,
  override val group: String,
  override val name: String,
  override val latestVersion: String,
  val lastUpdated: String? = null,
) : MavenArtifact() {
  val fullPath: String by lazy { "/${pkg.subject}/${pkg.repo}/$path" }
}
