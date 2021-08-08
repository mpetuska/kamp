import de.fayard.refreshVersions.core.versionFor

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
        val mdcVersion = versionFor("version.npm.material-components-web")
        api(npm("@material/ripple", mdcVersion))
        api(npm("@material/button", mdcVersion))
        api(npm("@material/icon-button", mdcVersion))
        api(npm("@material/top-app-bar", mdcVersion))
        api(npm("@material/chips", mdcVersion))
        api(npm("@material/card", mdcVersion))
        api(npm("@material/typography", mdcVersion))
        api(npm("@material/layout-grid", mdcVersion))
        api(compose.web.core)
        api(compose.runtime)
      }
    }
  }
}
