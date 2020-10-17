package scanner.domain.jc

import kotlinx.serialization.Serializable

@Serializable
data class JCPackage(
  val subject: String,
  val repo: String,
  val name: String,
) {
  val path by lazy { "/$subject/$repo/$name" }
}

