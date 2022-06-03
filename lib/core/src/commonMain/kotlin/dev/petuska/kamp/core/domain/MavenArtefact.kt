package dev.petuska.kamp.core.domain

import kotlinx.serialization.Transient

interface MavenArtefact {
  val group: String
  val name: String
  val latestVersion: String
  val releaseVersion: String?
  val versions: List<String>?
  val lastUpdated: Long?

  @Transient
  val version: String
    get() = releaseVersion ?: latestVersion

  @Transient
  val id: String get() = "$group:$name"

  @Transient
  val path: String get() = "$id:$version"
}
