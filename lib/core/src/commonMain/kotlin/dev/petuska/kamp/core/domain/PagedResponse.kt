package dev.petuska.kamp.core.domain

data class PagedResponse<T>(
  val data: List<T>,
  val page: Int,
  val next: String?,
  val prev: String?,
)
