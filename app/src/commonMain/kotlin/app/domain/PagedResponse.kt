package app.domain

import kotlinx.serialization.*

@Serializable
data class PagedResponse<T>(
  val data: List<T>,
  val next: String?,
  val prev: String?,
)
