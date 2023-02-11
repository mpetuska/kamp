plugins {
  id("convention.library-js")
  id("convention.library-jvm")
  id("convention.library-android")
}

kotlin {
  sourceSets {
    jvmCommonMain {}
    jvmCommonUnitTest {}
    jvmCommonInstrumentedTest {}
  }
}
