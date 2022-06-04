package ext

import org.gradle.api.Project
import org.gradle.api.provider.Property

@Suppress("LeakingThis")
abstract class MppAppExtension(override val project: Project) : ProjectExtension() {
  abstract val jvmMainClass: Property<String>
  abstract val fatJar: Property<Boolean>

  init {
    fatJar.convention(true)
  }
}
