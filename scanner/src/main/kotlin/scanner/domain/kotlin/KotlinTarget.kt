package scanner.domain.kotlin

import kotlinx.serialization.*

@Serializable
sealed class KotlinTarget {
  abstract val name: String
  abstract val category: String
  
  override fun toString(): String = "$category:$name"
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is KotlinTarget) return false
    
    if (name != other.name) return false
    if (category != other.category) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + category.hashCode()
    return result
  }
  
  
  @Serializable
  object Common : KotlinTarget() {
    override val name: String = "common"
    override val category = name
  }
  
  @Serializable
  sealed class JS(override val name: String) : KotlinTarget() {
    override val category = "js"
    
    @Serializable
    object Legacy : JS("legacy")
    
    @Serializable
    object IR : JS("ir")
  }
  
  @Serializable
  sealed class JVM(override val name: String) : KotlinTarget() {
    override val category = "jvm"
    
    @Serializable
    object Android : JVM("android")
    
    @Serializable
    object Java : JVM("java")
  }
  
  @Serializable
  class Native(override val name: String) : KotlinTarget() {
    override val category = "native"
  }
}
