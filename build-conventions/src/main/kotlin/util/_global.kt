@file:Suppress("PackageDirectoryMismatch")

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

infix fun <T> Property<T>.by(value: T) = set(value)
infix fun <T> Property<T>.by(value: Provider<T>) = set(value)
