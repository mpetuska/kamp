package app.domain

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T>(
  val data: List<T>,
  val page: Int,
  val next: String?,
  val prev: String?,
)
