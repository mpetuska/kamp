import gradle.kotlin.dsl.accessors._addcf92487a65cb5a0ce9e09d7d754d3.kotlin
import gradle.kotlin.dsl.accessors._addcf92487a65cb5a0ce9e09d7d754d3.sourceSets

plugins {
  id("convention.app-js")
  id("convention.app-jvm")
  id("convention.app-android")
}

kotlin {
  sourceSets {
    jvmCommonMain {}
    jvmCommonUnitTest {}
    jvmCommonInstrumentedTest {}
  }
}
