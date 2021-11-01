package dev.petuska.kamp.cli.domain

import kotlinx.serialization.SerialName

data class GradleModule(
    @SerialName("component") val component: Component? = null,
    @SerialName("createdBy") val createdBy: CreatedBy? = null,
    @SerialName("formatVersion") val formatVersion: String? = null,
    @SerialName("variants") val variants: List<Variant>? = null,
) {
  data class Component(
      @SerialName("url") val url: String? = null,
      @SerialName("attributes") val attributes: Attributes? = null,
      @SerialName("group") val group: String? = null,
      @SerialName("module") val module: String? = null,
      @SerialName("version") val version: String? = null,
  ) {

    data class Attributes(
        @SerialName("org.gradle.status") val orgGradleStatus: String? = null,
    )
  }

  data class CreatedBy(
      @SerialName("gradle") val gradle: Gradle? = null,
  ) {
    data class Gradle(
        @SerialName("buildId") val buildId: String? = null,
        @SerialName("version") val version: String? = null,
    )
  }

  data class Variant(
      @SerialName("attributes") val attributes: Attributes? = null,
      @SerialName("available-at") val availableAt: AvailableAt? = null,
      @SerialName("name") val name: String? = null,
  ) {
    data class Attributes(
        @SerialName("artifactType") val artifactType: String? = null,
        @SerialName("org.gradle.usage") val orgGradleUsage: String? = null,
        @SerialName("org.jetbrains.kotlin.js.compiler")
        val orgJetbrainsKotlinJsCompiler: String? = null,
        @SerialName("org.jetbrains.kotlin.native.target")
        val orgJetbrainsKotlinNativeTarget: String? = null,
        @SerialName("org.jetbrains.kotlin.platform.type")
        val orgJetbrainsKotlinPlatformType: String? = null,
    )

    data class AvailableAt(
        @SerialName("group") val group: String? = null,
        @SerialName("module") val module: String? = null,
        @SerialName("url") val url: String? = null,
        @SerialName("version") val version: String? = null,
    )
  }
}
