package ext

import org.gradle.api.provider.Property

interface JvmAppExtension : AppExtension {
  val mainClass: Property<String>
  val fatJar: Property<Boolean>
}
