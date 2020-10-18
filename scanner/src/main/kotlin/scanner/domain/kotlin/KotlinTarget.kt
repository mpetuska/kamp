package scanner.domain.kotlin

import kotlinx.serialization.*

@Serializable
class KotlinTarget private constructor(
  val platform: String,
  val category: String,
  val variant: String? = null,
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
  
  object Common {
    const val category = "common"
    operator fun invoke() = KotlinTarget(category, category)
  }
  
  object JS {
    const val category = "js"
    fun Legacy() = KotlinTarget(category, category, "legacy")
    fun IR() = KotlinTarget(category, category, "ir")
  }
  
  object JVM {
    const val category = "jvm"
    fun Java() = KotlinTarget("jvm", category)
    fun Android() = KotlinTarget("android", category)
  }
  
  object Native {
    const val category = "native"
    operator fun invoke(name: String) = KotlinTarget(name, category)
  }
}
