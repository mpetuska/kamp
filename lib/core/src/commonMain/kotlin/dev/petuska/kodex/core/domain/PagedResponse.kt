package dev.petuska.kodex.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T>(
  val data: List<T>,
  val prev: String?,
  val page: Int,
  val next: String?,
)
