package kamp.domain

import kotlinx.serialization.*

@Serializable
public class KotlinTarget private constructor(
  public val category: String,
  public val platform: String,
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

  public object Common {
    public const val category: String = "common"
    public operator fun invoke(): KotlinTarget = KotlinTarget(category, category)
  }

  public object JS {
    public const val category: String = "js"
    public fun Legacy(): KotlinTarget = KotlinTarget(category, "legacy")
    public fun IR(): KotlinTarget = KotlinTarget(category, "ir")
  }

  public object JVM {
    public const val category: String = "jvm"
    public fun Java(): KotlinTarget = KotlinTarget(category, "jvm")
    public fun Android(): KotlinTarget = KotlinTarget(category, "android")
  }

  public object Native {
    public const val category: String = "native"
    public operator fun invoke(platform: String): KotlinTarget = KotlinTarget(category, platform)
  }
}
