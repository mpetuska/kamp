plugins {
  id("convention.library-common")
}

kotlin {
  js(IR) {
    useEsModules()
    browser {
      commonWebpackConfig {
        cssSupport { enabled.set(true) }
        scssSupport { enabled.set(true) }
        configDirectory = project.rootDir.resolve("gradle/webpack.config.d")
      }
      testTask { useKarma {} }
    }
  }
}
