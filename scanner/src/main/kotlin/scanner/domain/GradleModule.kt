package scanner.domain

import kotlinx.serialization.*

@Serializable
data class GradleModule(
  @SerialName("component") val component: Component? = null,
  @SerialName("createdBy") val createdBy: CreatedBy? = null,
  @SerialName("formatVersion") val formatVersion: String? = null,
  @SerialName("variants") val variants: List<Variant>? = null,
) {
  @Serializable
  data class Component(
    @SerialName("url") val url: String? = null,
    @SerialName("attributes") val attributes: Attributes? = null,
    @SerialName("group") val group: String? = null,
    @SerialName("module") val module: String? = null,
    @SerialName("version") val version: String? = null,
  ) {

    @Serializable
    data class Attributes(
      @SerialName("org.gradle.status") val orgGradleStatus: String? = null,
    )
  }

  @Serializable
  data class CreatedBy(
    @SerialName("gradle") val gradle: Gradle? = null,
  ) {
    @Serializable
    data class Gradle(
      @SerialName("buildId") val buildId: String? = null,
      @SerialName("version") val version: String? = null,
    )
  }

  @Serializable
  data class Variant(
    @SerialName("attributes") val attributes: Attributes? = null,
    @SerialName("available-at") val availableAt: AvailableAt? = null,
    @SerialName("name") val name: String? = null,
  ) {
    @Serializable
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

    @Serializable
    data class AvailableAt(
      @SerialName("group") val group: String? = null,
      @SerialName("module") val module: String? = null,
      @SerialName("url") val url: String? = null,
      @SerialName("version") val version: String? = null,
    )
  }
}
