package ext

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer
import java.io.File

interface JsAppExtension : AppExtension {
  val outputFileName: Property<String>
  val distributionDir: Property<File>
  val devServer: Property<DevServer>
  fun devServer(action: Action<DevServer>) {
    devServer.get().also(action::execute).let(devServer::set)
  }
}
