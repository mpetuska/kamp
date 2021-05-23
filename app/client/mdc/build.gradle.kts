plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
}

kotlin {
  js {
    useCommonJs()
    browser()
  }
  sourceSets {
    named("jsMain") {
      dependencies {
        val mdcVersion = "11.0.0"
        api(npm("@material/ripple", mdcVersion))
        api(npm("@material/button", mdcVersion))
        api(npm("@material/icon-button", mdcVersion))
        api(compose.web.web)
        api(compose.runtime)
      }
    }
  }
}
