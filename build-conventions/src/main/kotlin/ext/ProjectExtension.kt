package ext

import org.gradle.api.Project

@Suppress("LeakingThis")
abstract class ProjectExtension {
  protected abstract val project: Project
}
