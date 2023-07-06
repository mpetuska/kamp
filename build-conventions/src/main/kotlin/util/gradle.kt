package util

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.nio.charset.Charset
import kotlin.properties.ReadOnlyProperty

object Git {
  val headCommitHash by lazy { execAndCapture("git rev-parse --verify HEAD") }
}

fun execAndCapture(cmd: String): String? {
  val child = Runtime.getRuntime().exec(cmd)
  child.waitFor()
  return if (child.exitValue() == 0) {
    child.inputStream.readAllBytes().toString(Charset.defaultCharset()).trim()
  } else {
    null
  }
}

fun linkedSourceSets(
  vararg sourceSets: String
) = ReadOnlyProperty<
  NamedDomainObjectContainer<KotlinSourceSet>,
  (action: Action<KotlinSourceSet>) -> Unit
  > { thisRef, property ->
  sourceSets.forEach {
    thisRef.named(it) {
      kotlin.srcDir("src/${property.name}/kotlin")
      resources.srcDir("src/${property.name}/resources")
    }
  }
  return@ReadOnlyProperty {
    sourceSets.forEach { ss -> thisRef.named(ss, it) }
  }
}
