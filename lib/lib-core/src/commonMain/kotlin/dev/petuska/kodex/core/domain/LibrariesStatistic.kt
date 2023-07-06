package dev.petuska.kodex.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class LibrariesStatistic(
  val date: String,
  val ts: Long,
  val countTotal: Int,
  val countByCategory: Map<String, Int>,
  val countByFamily: Map<String, Int>,
  val countByPlatform: Map<String, Int>,
) {
  @Suppress("PropertyName", "unused", "VariableNaming")
  val _id: String = date
}
