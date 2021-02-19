package kamp.domain

import kotlinx.serialization.*

@Serializable
public class KotlinTarget private constructor(
  public val platform: String,
  public val category: String,
  public val variant: String? = null,
) {

  override fun toString(): String = "$category:$platform${if (variant == null) "" else ":$variant"}"
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is KotlinTarget) return false

    if (platform != other.platform) return false
    if (category != other.category) return false
    if (variant != other.variant) return false

    return true
  }

  override fun hashCode(): Int {
    var result = variant.hashCode()
    result = 31 * result + category.hashCode()
    return result
  }

  public object Common {
    public const val category: String = "common"
    public operator fun invoke(): KotlinTarget = KotlinTarget(category, category)
  }

  public object JS {
    public const val category: String = "js"
    public fun Legacy(): KotlinTarget = KotlinTarget(category, category, "legacy")
    public fun IR(): KotlinTarget = KotlinTarget(category, category, "ir")
  }

  public object JVM {
    public const val category: String = "jvm"
    public fun Java(): KotlinTarget = KotlinTarget("jvm", category)
    public fun Android(): KotlinTarget = KotlinTarget("android", category)
  }

  public object Native {
    public const val category: String = "native"
    public operator fun invoke(name: String): KotlinTarget = KotlinTarget(name, category)
  }
}
