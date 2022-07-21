import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.enableSCSS

plugins {
  id("convention.common")
  kotlin("multiplatform")
  id("dev.petuska.klip")
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
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }
    commonMain {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:_")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
      }
    }
    commonTest {
      dependencies {
        implementation(project(":lib:test"))
      }
    }
  }
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
  }
}
