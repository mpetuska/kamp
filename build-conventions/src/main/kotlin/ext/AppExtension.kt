package ext

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import javax.inject.Inject

interface AppExtension : ExtensionAware {
  @get:Inject
  val project: Project
}
