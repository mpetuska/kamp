@file:Suppress("PackageDirectoryMismatch")

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import util.*

val NamedDomainObjectContainer<KotlinSourceSet>.jsMain
  get() = named<KotlinSourceSet>("jsMain")
val NamedDomainObjectContainer<KotlinSourceSet>.jsTest
  get() = named<KotlinSourceSet>("jsTest")
val NamedDomainObjectContainer<KotlinSourceSet>.jvmMain
  get() = named<KotlinSourceSet>("jvmMain")
val NamedDomainObjectContainer<KotlinSourceSet>.jvmTest
  get() = named<KotlinSourceSet>("jvmTest")
val NamedDomainObjectContainer<KotlinSourceSet>.jvmInstrumentedTest
  get() = named<KotlinSourceSet>("jvmInstrumentedTest")
val NamedDomainObjectContainer<KotlinSourceSet>.androidMain
  get() = named<KotlinSourceSet>("androidMain")
val NamedDomainObjectContainer<KotlinSourceSet>.androidUnitTest
  get() = named<KotlinSourceSet>("androidUnitTest")
val NamedDomainObjectContainer<KotlinSourceSet>.androidInstrumentedTest
  get() = named<KotlinSourceSet>(
    "androidInstrumentedTest"
  )

val NamedDomainObjectContainer<KotlinSourceSet>.jvmCommonMain by linkedSourceSets(
  "jvmMain",
  "androidMain",
)
val NamedDomainObjectContainer<KotlinSourceSet>.jvmCommonUnitTest by linkedSourceSets(
  "jvmTest",
  "androidUnitTest",
)
val NamedDomainObjectContainer<KotlinSourceSet>.jvmCommonInstrumentedTest by linkedSourceSets(
  "jvmInstrumentedTest",
  "androidInstrumentedTest",
)

infix fun <T> Property<T>.by(value: T) = set(value)
infix fun <T> Property<T>.by(value: Provider<T>) = set(value)
