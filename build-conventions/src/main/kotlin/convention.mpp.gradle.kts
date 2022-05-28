import util.enableSCSS

plugins {
  id("convention.common")
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  jvm()
  js(IR) {
    useCommonJs()
    enableSCSS(main = true, test = true)
    browser {
      testTask {
        useKarma {
          when (project.properties["kotlin.js.test.browser"]) {
            "firefox" -> useFirefox()
            "firefox-headless" -> useFirefoxHeadless()
            "firefox-developer" -> useFirefoxDeveloper()
            "firefox-developer-headless" -> useFirefoxDeveloperHeadless()
            "chrome" -> useChrome()
            "chrome-headless" -> useChromeHeadless()
            "chromium" -> useChromium()
            "chromium-headless" -> useChromiumHeadless()
            "safari" -> useSafari()
            "opera" -> useOpera()
            else -> usePhantomJS()
          }
        }
      }
    }
  }

  sourceSets {
    configureEach {
      languageSettings {
        optIn("kotlin.RequiresOptIn")
      }
    }
    commonMain {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:_")
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation("io.kotest:kotest-assertions-core:_")
        implementation("io.kotest:kotest-property:_")
      }
    }
    jvmTest {
      dependencies {
        implementation(kotlin("test-junit5"))
      }
    }
    jsTest {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}
