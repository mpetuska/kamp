package dev.petuska.kamp.core.domain

import kotlinx.serialization.Serializable

@Serializable
class KotlinTarget private constructor(
  val category: String,
  val platform: String,
) {

  override fun toString(): String = "$category:$platform"
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is KotlinTarget) return false

    if (platform != other.platform) return false
    if (category != other.category) return false

    return true
  }

  override fun hashCode(): Int {
    var result = platform.hashCode()
    result = 31 * result + category.hashCode()
    return result
  }

  object Common {
    const val category: String = "common"
    operator fun invoke(): KotlinTarget = KotlinTarget(category, category)
  }

  object JS {
    const val category: String = "js"
    fun Legacy(): KotlinTarget = KotlinTarget(category, "legacy")
    fun IR(): KotlinTarget = KotlinTarget(category, "ir")
  }

  object JVM {
    const val category: String = "jvm"
    fun Java(): KotlinTarget = KotlinTarget(category, "jvm")
    fun Android(): KotlinTarget = KotlinTarget(category, "android")
  }

  object Native {
    const val category: String = "native"
    operator fun invoke(platform: String): KotlinTarget = KotlinTarget(category, platform)
  }
}
