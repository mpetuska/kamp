package ext

import org.gradle.api.Project
import org.gradle.api.provider.Property

@Suppress("LeakingThis")
abstract class MppAppConvention(private val project: Project) {
  abstract val jvmMainClass: Property<String>
}
