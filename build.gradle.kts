plugins {
  id("com.github.jakemarsden.git-hooks")
  idea
}

gitHooks {
  setHooks(
    mapOf(
      "post-checkout" to "ktlintApplyToIdea",
      "pre-commit" to "ktlintFormat",
      "pre-push" to "ktlintCheck"
    )
  )
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
