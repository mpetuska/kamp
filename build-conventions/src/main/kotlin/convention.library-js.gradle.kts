plugins {
  id("convention.library-common")
}

kotlin {
  js(IR) {
    useCommonJs()
    browser {
      commonWebpackConfig {
        scssSupport { enabled.set(true) }
        configDirectory = project.rootDir.resolve("gradle/webpack.config.d")
      }
      testTask { useKarma {} }
    }
  }
  sourceSets {
    jsTest {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}
